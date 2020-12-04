package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseLandlordTask;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.model.repository.core.landlord.user.changeemail.BaseLandlordChangeEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Bastian Bliemeister.
 */
@Slf4j
@Component
public class DeleteUserEmailChangeTask extends BaseLandlordTask {

    @Autowired
    private BaseLandlordChangeEmailRepository changeEmailRepository;

    @Override
    public boolean run() {
        deleteEmailChanges();

        return true;
    }

    private void deleteEmailChanges() {
        log.info("Starting to delete expired EmailChange's ...");
        List<ChangeEmail> changeEmails = changeEmailRepository.findByValidUntilLessThan(new Date());

        for (ChangeEmail changeEmail : changeEmails) {
            changeEmailRepository.delete(changeEmail);
            log.info(changeEmail.getId() + " deleted [" + changeEmail.getToken() + "]");
        }
    }
}
