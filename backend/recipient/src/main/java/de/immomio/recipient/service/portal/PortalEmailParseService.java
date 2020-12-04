package de.immomio.recipient.service.portal;

import de.immomio.recipient.handler.PortalEmailHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PortalEmailParseService {

    private static final String PROTOCOL = "imaps";

    private static final String HOST = "smtp.gmail.com";

    private static final String INBOX = "INBOX";

    @Value("${imap.handlers.portal.email}")
    private String username;

    @Value("${imap.handlers.portal.password}")
    private String password;

    private final Properties receiverMailProperties;

    private final PortalEmailHandler receiver;

    @Autowired
    public PortalEmailParseService(@Qualifier("portalEmailProperty") Properties receiverMailProperties, PortalEmailHandler receiver) {
        this.receiverMailProperties = receiverMailProperties;
        this.receiver = receiver;
    }

    public Map<String, List<String>> parseEmails(LocalDate startDate, LocalDate enddate) throws MessagingException {
        log.info("start crawling not saved messages");

        Session session = Session.getDefaultInstance(receiverMailProperties);
        Store store = session.getStore(PROTOCOL);
        store.connect(HOST, username, password);
        Folder inbox = store.getFolder(INBOX);
        inbox.open(Folder.READ_ONLY);
        javax.mail.Message[] messages = inbox.getMessages();
        List<String> successMails = new ArrayList<>();
        List<String> erroneousEmails = new ArrayList<>();

        Stream<Message> messageStream = Arrays.stream(messages).filter(message -> {
            try {
                return filterMessage(startDate, enddate, message);
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
            }
            return false;
        });
        List<Message> filteredMessages = messageStream.collect(Collectors.toList());

        int i = 0;
        for (Message message : filteredMessages) {
            try {
                i++;
                log.info("COUNT + " + i + "OF " + filteredMessages.size());
                this.receiver.receive((MimeMessage) message);
                successMails.add(message.getReceivedDate() + " " + message.getSubject() + " " + message.getFrom()[0]);
            } catch (Exception e) {
                try {
                    erroneousEmails.add(message.getReceivedDate() + " " + message.getSubject() + " " + message.getFrom()[0]);
                } catch (MessagingException e1) {
                    log.error(e.getMessage(), e);
                }
                log.error(e.getMessage(), e);
            }
        }

        log.info("---------------------------");
        log.info("SUCCESS: " + successMails.size() + " mails parsed");
        log.info("FAIL: " + erroneousEmails.size() + " mails not parsed");
        log.info("---------------------------");
        log.info("end crawling not saved messages");

        Map<String, List<String>> result = new HashMap<>();
        result.put("success", successMails);
        result.put("erroneous", erroneousEmails);

        return result;
    }

    private boolean filterMessage(LocalDate startDate, LocalDate enddate, Message message) throws MessagingException {
        LocalDateTime messageDate = message.getReceivedDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return (messageDate.isAfter(startDate.atStartOfDay())) && messageDate.isBefore(enddate.plusDays(1).atStartOfDay());
    }
}
