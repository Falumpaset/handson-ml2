package de.immomio.service.propertySearcher.onboarding;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.model.repository.propertysearcher.note.PropertysearcherNoteRepository;
import de.immomio.model.repository.propertysearcher.schufa.PropertySearcherLandlordSchufaJobRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.customquestion.CustomQuestionResponseRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureResponseRepository;
import de.immomio.model.repository.shared.tenant.PropertyTenantRepository;
import de.immomio.service.propertysearcher.PropertySearcherSearchUntilCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PropertySearcherUserProfileMergeService {

    private final PropertySearcherUserProfileRepository userProfileRepository;
    private final PropertysearcherNoteRepository noteRepository;
    private final CustomQuestionResponseRepository customQuestionResponseRepository;
    private final PropertySearcherLandlordSchufaJobRepository schufaJobRepository;
    private final PropertyApplicationRepository applicationRepository;
    private final PropertyTenantRepository propertyTenantRepository;
    private final SelfDisclosureResponseRepository selfDisclosureResponseRepository;
    private final PropertySearcherSearchUntilCalculationService searchUntilService;

    @Autowired
    public PropertySearcherUserProfileMergeService(PropertySearcherUserProfileRepository userProfileRepository,
            PropertysearcherNoteRepository noteRepository,
            CustomQuestionResponseRepository customQuestionResponseRepository,
            PropertySearcherLandlordSchufaJobRepository schufaJobRepository,
            PropertyApplicationRepository applicationRepository,
            PropertyTenantRepository propertyTenantRepository,
            SelfDisclosureResponseRepository selfDisclosureResponseRepository,
            PropertySearcherSearchUntilCalculationService searchUntilService) {
        this.userProfileRepository = userProfileRepository;
        this.noteRepository = noteRepository;
        this.customQuestionResponseRepository = customQuestionResponseRepository;
        this.schufaJobRepository = schufaJobRepository;
        this.applicationRepository = applicationRepository;
        this.propertyTenantRepository = propertyTenantRepository;
        this.selfDisclosureResponseRepository = selfDisclosureResponseRepository;
        this.searchUntilService = searchUntilService;
    }

    public PropertySearcherUserProfile merge(PropertySearcherUser user, PropertySearcherUserProfileData userProfileData, PropertySearcherUserProfile userProfile) {
        if (userProfile == null) {
            userProfile = userProfileRepository.findFirstByUserAndTypeOrderByCreatedDesc(user, PropertySearcherUserProfileType.GUEST);
        }
        if (userProfile == null) {
            userProfile = userProfileRepository.findFirstByUserAndTypeOrderByCreatedDesc(user, PropertySearcherUserProfileType.ANONYMOUS);
        }
        if (userProfile == null) {
            return user.getMainProfile();
        }

        if (userProfileData != null) {
            userProfile.setData(userProfileData);
        }

        userProfile.setSearchUntil(searchUntilService.getSearchUntil(userProfile));

        return merge(userProfile, user);
    }

    private PropertySearcherUserProfile merge(PropertySearcherUserProfile userProfile, PropertySearcherUser user) {
        if (userProfile.getType() == PropertySearcherUserProfileType.MAIN) {
            throw new ApiValidationException();
        }

        PropertySearcherUserProfile mainProfile = merge(user.getMainProfile(), userProfile);
        List<PropertySearcherUserProfile> userProfiles = user.getAllProfilesWithoutMain();

        mergeNotes(mainProfile);
        mergeCustomQuestionResponse(mainProfile, userProfile);
        mergeSchufaJobs(mainProfile, userProfiles);
        mergeApplications(mainProfile, userProfiles);
        mergePropertyTenants(mainProfile, userProfiles);
        mergeSelfDisclosureResponses(mainProfile, userProfile);

        userProfileRepository.deleteAll(userProfiles);
        user.getProfiles().removeAll(userProfiles);

        return userProfile;
    }

    private void mergeNotes(PropertySearcherUserProfile mainProfile) {
        List<Note> notes = noteRepository.findAllByUser(mainProfile.getUser());
        Map<LandlordCustomer, List<Note>> notesByCustomer = notes.stream()
                .collect(Collectors.groupingBy(Note::getCustomer, Collectors.mapping(Function.identity(), Collectors.toList())));

        notesByCustomer.forEach((key, value) -> {
            Note note = new Note();
            note.setCustomer(key);
            note.setUserProfile(mainProfile);
            note.setRating(value.stream().map(Note::getRating).mapToDouble(Double::doubleValue).average().orElse(0));
            note = noteRepository.save(note);

            noteRepository.switchNoteCommentNotes(note, value);
        });

        noteRepository.deleteAll(notes);
    }

    private void mergeCustomQuestionResponse(PropertySearcherUserProfile mainProfile, PropertySearcherUserProfile chosenUserProfile) {
        Map<CustomQuestion, CustomQuestionResponse> customQuestionResponses = chosenUserProfile.getCustomQuestionResponses()
                .stream()
                .collect(Collectors.toMap(CustomQuestionResponse::getCustomQuestion, customQuestionResponse -> {
                    customQuestionResponse.setUserProfile(mainProfile);
                    return customQuestionResponse;
                }));

        mainProfile.getUser()
                .getProfiles()
                .stream()
                .map(PropertySearcherUserProfile::getCustomQuestionResponses)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(CustomQuestionResponse::getCreated).reversed())
                .forEach(customQuestionResponse -> {
                    if (!customQuestionResponses.containsKey(customQuestionResponse.getCustomQuestion())) {
                        customQuestionResponse.setUserProfile(mainProfile);
                        customQuestionResponses.put(customQuestionResponse.getCustomQuestion(), customQuestionResponse);
                    }
                });

        if (!customQuestionResponses.values().isEmpty()) {
            customQuestionResponseRepository.saveAll(customQuestionResponses.values());
        }
        List<PropertySearcherUserProfile> userProfiles = mainProfile.getUser().getAllProfilesWithoutMain();
        if (!userProfiles.isEmpty()) {
            customQuestionResponseRepository.deleteAllByUserProfileIn(userProfiles);
        }
    }

    private void mergeSchufaJobs(PropertySearcherUserProfile mainProfile, List<PropertySearcherUserProfile> userProfiles) {
        List<LandlordSchufaJob> schufaJobs = schufaJobRepository.findByUserProfileIn(userProfiles);
        schufaJobs.forEach(landlordSchufaJob -> landlordSchufaJob.setUserProfile(mainProfile));
        schufaJobRepository.saveAll(schufaJobs);
    }

    private void mergeApplications(PropertySearcherUserProfile mainProfile, List<PropertySearcherUserProfile> userProfiles) {
        List<PropertyApplication> applications = applicationRepository.findByUserProfileIn(userProfiles);
        applications.forEach(propertyApplication -> propertyApplication.setUserProfile(mainProfile));
        applicationRepository.saveAll(applications);
    }

    private void mergePropertyTenants(PropertySearcherUserProfile mainProfile, List<PropertySearcherUserProfile> userProfiles) {
        List<PropertyTenant> propertyTenants = propertyTenantRepository.findAllByUserProfileIn(userProfiles);
        propertyTenants.forEach(propertyTenant -> propertyTenant.setUserProfile(mainProfile));
        propertyTenantRepository.saveAll(propertyTenants);
    }

    private void mergeSelfDisclosureResponses(PropertySearcherUserProfile mainProfile, PropertySearcherUserProfile userProfile) {
        List<SelfDisclosureResponse> selfDisclosureResponses = selfDisclosureResponseRepository.findAllByUserProfile(userProfile);
        selfDisclosureResponses.forEach(selfDisclosureResponse -> selfDisclosureResponse.setUserProfile(mainProfile));
        selfDisclosureResponseRepository.saveAll(selfDisclosureResponses);
    }

    private PropertySearcherUserProfile merge(PropertySearcherUserProfile mainProfile, PropertySearcherUserProfile userProfile) {
        mainProfile.setAddress(userProfile.getAddress());
        mainProfile.setData(userProfile.getData());

        List<LandlordCustomer> tenantPoolCustomers = mainProfile.getUser()
                .getProfiles()
                .stream()
                .map(PropertySearcherUserProfile::getTenantPoolCustomer)
                .distinct()
                .collect(Collectors.toList());

        if (tenantPoolCustomers.size() == 1) {
            mainProfile.setTenantPoolCustomer(tenantPoolCustomers.get(0));
        } else {
            mainProfile.setTenantPoolCustomer(null);
        }

        return userProfileRepository.save(mainProfile);
    }
}
