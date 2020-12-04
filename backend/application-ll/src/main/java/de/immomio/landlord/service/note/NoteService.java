package de.immomio.landlord.service.note;

import de.immomio.constants.exceptions.ApiNotFoundException;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.note.NoteBean;
import de.immomio.data.shared.bean.note.NoteCreateBean;
import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.note.NoteComment;
import de.immomio.landlord.service.application.LandlordPropertyApplicationService;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.propertysearcher.LandlordPropertySearcherUserProfileRepository;
import de.immomio.model.repository.shared.note.NoteCommentRepository;
import de.immomio.model.repository.shared.note.NoteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Service
public class NoteService {
    public static final String USER_PROFILE_NOT_FOUND_L = "USER_PROFILE_NOT_FOUND_L";
    private final NoteRepository noteRepository;
    private final NoteConverter noteConverter;
    private final NoteCommentRepository noteCommentRepository;
    private final LandlordPropertyApplicationService applicationService;
    private PropertyService propertyService;
    private LandlordPropertySearcherUserProfileRepository userProfileRepository;
    private final UserSecurityService userSecurityService;

    @Autowired
    public NoteService(NoteRepository noteRepository,
            NoteCommentRepository noteCommentRepository,
            NoteConverter noteConverter,
            LandlordPropertyApplicationService applicationService,
            PropertyService propertyService,
            LandlordPropertySearcherUserProfileRepository userProfileRepository,
            UserSecurityService userSecurityService) {
        this.noteRepository = noteRepository;
        this.noteCommentRepository = noteCommentRepository;
        this.noteConverter = noteConverter;
        this.applicationService = applicationService;
        this.propertyService = propertyService;
        this.userProfileRepository = userProfileRepository;
        this.userSecurityService = userSecurityService;
    }

    public NoteBean createOrUpdateNote(NoteCreateBean createBean) throws NotAuthorizedException {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();

        PropertySearcherUserProfile userProfile = userProfileRepository.findById(createBean.getUserProfileId())
                .orElseThrow(() -> new ApiNotFoundException(USER_PROFILE_NOT_FOUND_L));

        Property property = propertyService.findById(createBean.getPropertyId());
        Note note = getOrCreateNote(customer, userProfile);
        if (createBean.getRating() != null) {
            note.setRating(createBean.getRating());
        }
        if (StringUtils.isNotBlank(createBean.getComment())) {
            addCommentsToNote(createBean.getComment(), property, note);
        }
        Note savedNote = noteRepository.save(note);
        applicationService.tagSeen(userProfile, property, true);
        return noteConverter.convertNoteToNoteBean(savedNote, savedNote.getComments());
    }

    public NoteBean findByPSUserProfileIdAndCustomer(Long userProfileId, LandlordCustomer customer) {
        Optional<Note> noteOpt = noteRepository.findByPSUserProfileIdAndCustomerId(userProfileId, customer.getId());
        return noteOpt.map(note -> {
            List<NoteComment> comments = noteCommentRepository.findByNote(note);
            return noteConverter.convertNoteToNoteBean(note, comments);
        }).orElse(new NoteBean());
    }

    private void addCommentsToNote(String comment, Property property, Note note) {
        NoteComment noteComment = new NoteComment();
        noteComment.setComment(comment);
        noteComment.setUser(userSecurityService.getPrincipalUser());
        noteComment.setProperty(property);

        noteComment.setNote(note);
        note.getComments().add(noteComment);
    }

    private Note getOrCreateNote(LandlordCustomer customer, PropertySearcherUserProfile propertySearcherUserProfile) {
        return noteRepository.findByPSUserProfileIdAndCustomerId(propertySearcherUserProfile.getId(), customer.getId()).orElseGet(() -> {
            Note newNote = new Note();
            newNote.setUserProfile(propertySearcherUserProfile);
            newNote.setCustomer(customer);
            newNote.setRating(0);
            return newNote;
        });
    }

}
