package de.immomio.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.FileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.persistence.Transient;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Getter
@Setter
public class MailMessage {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String CONTENT_TYPE_PARAM = "Content-Type";
    private static final String X_SMTP_API = "x-smtpapi";
    private static final String RELATED = "related";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-transfer-encoding";
    private static final String CONTENT_ID = "Content-ID";
    private static final String BASE_64 = "base64";

    private String toEmail;

    private String toCc;

    private List<String> toBcc;

    private String fromEmail = "";

    private String fromName = "";

    private String subject = "";

    private String messageBody = "";

    private Map<String, Object> xSmtpApi = new HashMap<>();

    @Transient
    private Map<String, File> attachments = new HashMap<>();

    @Transient
    private boolean sendAttachmentsInBase64 = false;

    @Transient
    public MimeMessagePreparator preparator() {
        return mimeMessage -> {
            mimeMessage.setHeader(CONTENT_TYPE_PARAM, CONTENT_TYPE);
            mimeMessage.setRecipients(RecipientType.TO, toEmail);

            if (xSmtpApi != null && xSmtpApi.size() > 0) {
                ObjectMapper mapper = new ObjectMapper();

                mimeMessage.addHeader(X_SMTP_API, mapper.writeValueAsString(xSmtpApi));
                System.out.println(mapper.writeValueAsString(xSmtpApi));
            }

            if (toCc != null) {
                mimeMessage.setRecipients(RecipientType.CC, toCc);
            }

            if (toBcc != null) {
                for (String email : toBcc) {
                    mimeMessage.setRecipients(RecipientType.BCC, email);
                }
            }

            mimeMessage.setSentDate(new Date());
            mimeMessage.setSubject(subject, DEFAULT_ENCODING);
            fromEmail = fromEmail.replace(" ", "");
            if (StringUtils.isNotEmpty(fromEmail)) {
                if (StringUtils.isNotEmpty(fromName)) {
                    mimeMessage.setFrom(new InternetAddress(fromEmail, fromName, "UTF8"));
                } else {
                    mimeMessage.setFrom(fromEmail);
                }
            }

            MimeMultipart multipart = new MimeMultipart(RELATED);
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(messageBody, CONTENT_TYPE);
            multipart.addBodyPart(messageBodyPart);

            for (Map.Entry<String, File> attachment : attachments.entrySet()) {
                File file = attachment.getValue();
                String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(file);

                messageBodyPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setHeader(CONTENT_TYPE_PARAM, contentType);
                messageBodyPart.setHeader(CONTENT_ID, attachment.getKey());

                if (isSendAttachmentsInBase64()) {
                    messageBodyPart.setHeader(CONTENT_TRANSFER_ENCODING, BASE_64);
                }

                messageBodyPart.setFileName(attachment.getKey());
                multipart.addBodyPart(messageBodyPart);
            }

            mimeMessage.setContent(multipart);
        };
    }

    @Override
    public String toString() {
        return "Message [toEmail=" + toEmail + ", toCc=" + toCc + ", toBcc="
                + toBcc + ", fromEmail=" + fromEmail + ", subject=" + subject
                + ", messageBody=" + messageBody + "]";
    }

}
