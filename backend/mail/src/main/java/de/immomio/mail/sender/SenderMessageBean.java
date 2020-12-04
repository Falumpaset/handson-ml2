package de.immomio.mail.sender;

import de.immomio.mail.sender.templates.MailTemplate;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Bastian Bliemeister
 */
@Getter
@Setter
public abstract class SenderMessageBean {

    private String toEmail;

    private Long customerId;

    private Long userId;

    private MailTemplate template;

    private String subject;

    private Object[] subjectFormat;

    private Map<String, Object> data;

    private Map<String, String> attachments;

    private String fromEmail;

    private Long customizerCustomerId;

    private boolean sendAttachmentsInBase64 = false;


    @Override
    public String toString() {
        return "SenderMessageBean {" +
                       "\n\t" +
                       "attachments = " + attachments +
                       ",\n\t" +
                       "customerId = " + customerId +
                       ",\n\t" +
                       "customizerCustomerId = " + customizerCustomerId +
                       ",\n\t" +
                       "data = " + data +
                       ",\n\t" +
                       "fromEmail = '" + fromEmail + '\'' +
                       ",\n\t" +
                       "sendAttachmentsInBase64 = " + sendAttachmentsInBase64 +
                       ",\n\t" +
                       "subject = '" + subject + '\'' +
                       ",\n\t" +
                       "subjectFormat = " + Arrays.toString(subjectFormat) +
                       ",\n\t" +
                       "template = " + template +
                       ",\n\t" +
                       "toEmail = '" + toEmail + '\'' +
                       ",\n\t" +
                       "userId = " + userId +
                       "\n}";
    }
}
