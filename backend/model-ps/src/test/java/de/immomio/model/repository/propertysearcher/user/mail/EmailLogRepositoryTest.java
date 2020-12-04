package de.immomio.model.repository.propertysearcher.user.mail;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.email.EmailLog;
import de.immomio.data.propertysearcher.entity.user.email.MailEventType;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.propertysearcher.customer.PropertySearcherCustomerRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareEmailLogs;
import static org.junit.Assert.assertTrue;

public class EmailLogRepositoryTest extends BaseDataJpaTest {
    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private PropertySearcherUserRepository propertySearcherUserRepository;

    @Autowired
    private PropertySearcherCustomerRepository customerRepository;

    {
        describe("create log", () -> it("create log - success", () -> {
            PropertySearcherUser propertySearcherUser = TestHelper.generatePropertySearcherUser();
            propertySearcherUserRepository.save(propertySearcherUser);
            EmailLog emailLog = TestHelper.generateEmailLog(
                    propertySearcherUser);
            EmailLog savedEmailLog = emailLogRepository.save(emailLog);

            compareEmailLogs(emailLog, savedEmailLog);

        }));

        describe("find log", () -> it("find log by user and EventType", () -> {
            PropertySearcherCustomer propertySearcherCustomer1 = TestHelper.generateCustomer();
            customerRepository.save(propertySearcherCustomer1);

            PropertySearcherUser propertySearcherUser1 = TestHelper.generatePropertySearcherUser(propertySearcherCustomer1);
            propertySearcherUserRepository.save(propertySearcherUser1);

            EmailLog log1 = TestHelper.generateEmailLog(propertySearcherUser1);
            EmailLog savedLog = emailLogRepository.save(log1);

            Optional<EmailLog> emailLog = emailLogRepository.findByUserAndEventType(
                    propertySearcherUser1, MailEventType.PROFILE_COMPLETION);
            compareEmailLogs(emailLog.orElseThrow(() -> new Exception("email log not found")), savedLog);

        }));

        describe("delete log", () -> it("delete log by ID - success", () -> {
            PropertySearcherUser user1 = TestHelper.generatePropertySearcherUser();
            propertySearcherUserRepository.save(user1);
            PropertySearcherUser user2 = TestHelper.generatePropertySearcherUser();
            propertySearcherUserRepository.save(user2);

            EmailLog emailLog1 = TestHelper.generateEmailLog(user1);
            EmailLog emailLog2 = TestHelper.generateEmailLog(user2);
            EmailLog savedEmailLog1 = emailLogRepository.save(emailLog1);
            EmailLog savedEmailLog2 = emailLogRepository.save(emailLog2);

            compareEmailLogs(emailLogRepository.findById(savedEmailLog1.getId()).get(), savedEmailLog1);
            compareEmailLogs(emailLogRepository.findById(savedEmailLog2.getId()).get(), savedEmailLog2);

            emailLogRepository.deleteById(savedEmailLog1.getId());

            assertTrue(emailLogRepository.findById(savedEmailLog1.getId()).isEmpty());
            compareEmailLogs(emailLogRepository.findById(emailLog2.getId()).get(), savedEmailLog2);

        }));
    }
}

