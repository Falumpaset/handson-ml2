package de.immomio.service.landlord.user;

import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.constants.customer.Title;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.service.landlord.LandlordChangePasswordService;
import de.immomio.service.landlord.LandlordOnboardingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordUserImportService {

    public static final String INVALID_USERTYPE_L = "INVALID_USERTYPE_L";
    public static final String SEPARATOR = ";";
    public static final String NOT_ENOUGH_AGENTS_AVAILABLE_L = "NOT_ENOUGH_AGENTS_AVAILABLE_L";
    private LandlordUserRepository landlordUserRepository;

    private final LandlordOnboardingService landlordOnboardingService;

    private final LandlordChangePasswordService changePasswordService;

    private final LandlordUserService landlordUserService;

    @Autowired
    public LandlordUserImportService(LandlordUserRepository landlordUserRepository,
            LandlordOnboardingService landlordOnboardingService,
            LandlordChangePasswordService changePasswordService,
            LandlordUserService landlordUserService) {
        this.landlordUserRepository = landlordUserRepository;
        this.landlordOnboardingService = landlordOnboardingService;
        this.changePasswordService = changePasswordService;
        this.landlordUserService = landlordUserService;
    }

    public void importUsers(LandlordCustomer customer, MultipartFile multipartFile, LandlordUsertype usertype, Boolean enableUsers) throws IOException {
        if (usertype == LandlordUsertype.COMPANYADMIN) {
            throw new ApiValidationException(INVALID_USERTYPE_L);
        }

        List<LandlordUser> usersToSave = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));

        reader.readLine();
        while (reader.ready()) {
            String line = reader.readLine();

            String[] split = line.split(SEPARATOR);
            String name = split[0];
            String surname = split[1];
            String email = split[2];


            if (StringUtils.isBlank(name) || StringUtils.isBlank(surname) || StringUtils.isBlank(email)) {
                continue;
            }

            LandlordUser foundUser = landlordUserRepository.findByEmailIgnoreCase(email);
            if (foundUser != null) {
                continue;
            }

            email = email.toLowerCase();

            LandlordUserProfile newAgentProfile = new LandlordUserProfile();

            newAgentProfile.setFirstname(surname);
            newAgentProfile.setName(name);
            newAgentProfile.setTitle(Title.NONE);

            LandlordUser user = new LandlordUser();
            user.setCustomer(customer);
            user.setUsertype(usertype);
            user.setEmail(email);

            user.setEnabled(enableUsers);
            user.setProfile(newAgentProfile);
            usersToSave.add(user);

        }

        boolean enoughAgentsFree = landlordUserService.freeAgentSlotCheck(customer, usersToSave.size());
        if (!enoughAgentsFree && enableUsers) {
            throw new ApiValidationException(NOT_ENOUGH_AGENTS_AVAILABLE_L);
        }

        usersToSave.forEach(user -> {
            LandlordUser savedUser = landlordUserRepository.save(user);
            landlordOnboardingService.createInKeycloak(new CustomerUserBean(savedUser), enableUsers);
            if (enableUsers) {
                changePasswordService.newUser(savedUser);
            }

        });

    }


}
