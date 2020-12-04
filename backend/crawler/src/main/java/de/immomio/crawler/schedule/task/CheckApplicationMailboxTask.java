package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.shared.entity.email.MailMessageBean;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Bastian Bliemeister.
 */
@Component
@Slf4j
public class CheckApplicationMailboxTask extends BaseTask {

    private static final String APPLICATION_MAILBOX_WARNING_SUBJECT = "application.mailbox.warning.subject";

    private static final String MSGS = "msgs";
    private static final String INBOX = "INBOX";

    private final LandlordMailSender mailSender;

    @Value("${mailbox.application.protocol}")
    private String protocol;

    @Value("${mailbox.application.server}")
    private String server;

    @Value("${mailbox.application.port}")
    private Integer port;

    @Value("${mailbox.application.user}")
    private String user;

    @Value("${mailbox.application.password}")
    private String password;

    @Value("${email.error}")
    private String errorsEmail;

    @Value("${email.bugs}")
    private String bugsEmail;

    @Value("${email.monitoring}")
    private String monitoringEmail;

    @Value("${mailbox.application.unreadwarning}")
    private Integer unreadWarning;

    @Autowired
    public CheckApplicationMailboxTask(LandlordMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean run() {

        Message[] messages;
        try {
            Session session = Session.getDefaultInstance(new Properties());
            Store store = session.getStore(protocol);
            store.connect(server, port, user, password);
            Folder inbox = store.getFolder(INBOX);
            inbox.open(Folder.READ_ONLY);

            messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        } catch (MessagingException e) {
            log.error("Failed checking the content of the Application-Mailbox.", e);

            return false;
        }

        Arrays.sort(messages, (m1, m2) -> {
            try {
                return m2.getSentDate().compareTo(m1.getSentDate());
            } catch (MessagingException e) {
                log.error("Failed checking the content of the Application-Mailbox.", e);
                throw new RuntimeException(e);
            }
        });

        log.info("Application-Mailbox contains " + messages.length + " unread mails.");

        if (messages.length >= unreadWarning) {
            return send(messages);
        }

        return true;
    }

    private boolean send(Message[] messages) {
        Map<String, Object> model = generateMessageWarningModel(messages);

        mailSender.send(errorsEmail, MailTemplate.APPMAILBOX_WARNING, APPLICATION_MAILBOX_WARNING_SUBJECT, model);
        mailSender.send(bugsEmail, MailTemplate.APPMAILBOX_WARNING, APPLICATION_MAILBOX_WARNING_SUBJECT, model);
        mailSender.send(monitoringEmail, MailTemplate.APPMAILBOX_WARNING, APPLICATION_MAILBOX_WARNING_SUBJECT, model);

        log.info("Warning-Mails sent to " + errorsEmail + ", " + bugsEmail + " and " + monitoringEmail + " ...");

        return true;
    }

    private Map<String, Object> generateMessageWarningModel(Message[] messages) {
        Map<String, Object> model = new HashMap<>();
        List<MailMessageBean> mailMessageBeans = new ArrayList<>();
        for (Message message : messages) {
            try {
                mailMessageBeans.add(new MailMessageBean(message.getSubject(), message.getSentDate()));
            } catch (MessagingException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        model.put(MSGS, mailMessageBeans);

        return model;
    }
}
