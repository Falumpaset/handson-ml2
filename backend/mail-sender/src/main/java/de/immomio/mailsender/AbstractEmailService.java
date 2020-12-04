package de.immomio.mailsender;

import com.google.common.io.Files;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.common.zip.FileZipper;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.MailMessage;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.sendgrid.SendgridTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Johannes Hiemer, Bastian Bliemeister, Maik Kingma
 */

@Slf4j
@Service
public abstract class AbstractEmailService<T extends AbstractS3FileManager> {

    private static final String UTF_8 = "UTF-8";
    private static final String TEST_AUTOMATION_EMAIL_ADDRESS_END = "email.ghostinspector.com";
    private static final String LOCALE = "locale";
    private static final String REPORT = "report";
    private static final String PRODUCTION = "production";
    private static final String IMMOMIO_DE = "@immomio.de";
    private static final String P_TAG = "<p>";
    private static final String P_TAG_BREAK = "</p>";
    private static final String DEVELOPMENT = "development";
    private static final String INTEGRATION = "integration";
    private static final String IM_20_MAILS_IMMOMIO_DE = "im20_mails@immomio.de";
    private static final String STAGING = "staging";
    private static final String S3FILE_KEY = "s3file";
    private static final String HOTFIX = "hotfix";

    @Resource
    private JavaMailSender mailSender;

    private final Environment env;

    private final VelocityInternalizationLoader velocityInternationalizationLoader;

    private final FileZipper fileZipper;

    private SendgridTemplateService sendgridTemplateService;

    @Value("${email.error}")
    private String errorEmail;

    @Value("${mail.sender.address}")
    private String senderAddress;

    @Value("${mail.sender.name}")
    private String senderName;

    @Value("${mail.sender.logging}")
    private String internalLoggingEmail;

    @Value("${mail.logging}")
    private Boolean internalLogging;

    @Value("${mail.sender.invoice-logging}")
    private String internalInvoiceLogging;

    public AbstractEmailService(
            Environment env,
            VelocityInternalizationLoader velocityInternationalizationLoader,
            FileZipper fileZipper
    ) {
        this.env = env;
        this.velocityInternationalizationLoader = velocityInternationalizationLoader;
        this.fileZipper = fileZipper;
    }

    @Autowired
    public void setSendgridTemplateService(SendgridTemplateService sendgridTemplateService) {
        this.sendgridTemplateService = sendgridTemplateService;
    }

    public abstract T getS3FileManager();

    public abstract String getFilerUrl();

    public MailMessage create(String toEmail, String fromEmail, String toCc, MailTemplate template, String subject,
                              Map<String, Object> data, Map<String, String> attachments, LandlordCustomer customer) {
        MailMessage mailMessage;
        try {
            mailMessage = new MailMessage();
            mailMessage.setToEmail(toEmail);
        } catch (Exception e) {
            log.error("Error creating E-Mail-Message.", e);

            mailMessage = new MailMessage();
            mailMessage.setToEmail(errorEmail);
        }

        mailMessage.setSubject(subject);

        if (fromEmail == null) {
            mailMessage.setFromEmail(getSenderMailAddress(data));
        } else {
            mailMessage.setFromEmail(fromEmail);
        }

        mailMessage.setFromName(getSenderName(data));

        if (toCc != null) {
            mailMessage.setToCc(toCc);
        }

        // BCC
        List<String> toBcc = new ArrayList<>();

        if (internalLogging != null && internalLogging && internalLoggingEmail != null) {
            toBcc.add(internalLoggingEmail);
        }

        String bccTmp = checkBcc(template);
        if (bccTmp != null) {
            toBcc.add(bccTmp);
        }

        mailMessage.setToBcc(toBcc);

        if (getFilerUrl() != null) {
            data.put(REPORT, getFilerUrl());
        }

        if (!data.containsKey(LOCALE)) {
            data.put(LOCALE, LocaleContextHolder.getLocale());
        }

        String body = getBody(template, customer, data);

        mailMessage.setMessageBody(body);

        mailMessage.setAttachments(getAttachments(attachments));

        mailMessage.getXSmtpApi().put("category", template.getCategories());

        return mailMessage;
    }

    private String getBody(MailTemplate template, LandlordCustomer customer, Map<String, Object> data) {
        String body;
        if (StringUtils.isBlank(template.getSendGridTemplate())) {
            body = velocityInternationalizationLoader.merge(template.getTemplateFile(), UTF_8,
                    data);
        } else {
            Locale locale = (Locale) data.getOrDefault(LOCALE, Locale.GERMANY);
            body = sendgridTemplateService.getSendGridHtml(template, customer, locale, data);
        }

        return body;
    }

    private Map<String, File> getAttachments(Map<String, String> attachments) {
        Map<String, File> map = new HashMap<>();

        if (attachments == null || attachments.isEmpty() || !attachments.containsKey(S3FILE_KEY)) {
            return map;
        }

        String s3File = attachments.remove(S3FILE_KEY);
        File downloadFolder = Files.createTempDir();
        FileStoreObject fileStoreObject = FileStoreObject.parse(s3File);
        File downloadedFile = getS3FileManager().downloadFileStoreObject(fileStoreObject, downloadFolder);
        File zipFolder = Files.createTempDir();

        fileZipper.unzipFile(downloadedFile.getAbsolutePath(), zipFolder);
        Arrays.stream(zipFolder.listFiles()).forEach(file -> map.put(file.getName(), file));

        FileUtilities.forceDelete(downloadFolder);

        for (Entry<String, String> entry : attachments.entrySet()) {
            File file = new File(zipFolder, entry.getValue());

            if (!file.exists()) {
                log.error("AttachmentFile does not exists ... " + entry.toString());
                continue;
            }

            map.put(entry.getKey(), file);
        }

        return map;
    }

    public void send(MailMessage mailMessage)  {
        //If the active profile is unequal production, forward all company external emails to im20 post box
        if (!Arrays.asList(env.getActiveProfiles()).contains(PRODUCTION)
                && !mailMessage.getToEmail().endsWith(IMMOMIO_DE)
        ) {
            mailMessage.setToCc(null);

            if (mailMessage.getToEmail().contains(TEST_AUTOMATION_EMAIL_ADDRESS_END)) {
                mailMessage.setToBcc(Collections.singletonList(IM_20_MAILS_IMMOMIO_DE));
                sendAndCleanAttachments(mailMessage);

                return;
            }

            if (isDevEnv()) {
                String text = mailMessage.getMessageBody();
                text = P_TAG + mailMessage.getToEmail() + P_TAG_BREAK + text;
                mailMessage.setMessageBody(text);
                mailMessage.setToEmail(IM_20_MAILS_IMMOMIO_DE);
                mailMessage.setToBcc(null);
            }
        }

        sendAndCleanAttachments(mailMessage);
    }

    private String getSenderMailAddress(Map<String, Object> data) {
        String[] split = senderAddress.split("@");
        String senderAddressLocal = split[0];
        String senderAddressDomain = split[1];
        String local = (String) data.getOrDefault(ModelParams.MODEL_SENDER_ADDRESS_PREFIX, senderAddressLocal);
        return String.format("%s@%s", local, senderAddressDomain);
    }

    private String getSenderName(Map<String, Object> data) {
        String senderNamePrefix = (String) data.get(ModelParams.MODEL_SENDER_NAME_PREFIX);
        if (StringUtils.isNotBlank(senderNamePrefix)) {
            return String.format("%s via %s", senderNamePrefix, senderName);
        }
        return senderName;
    }

    private void sendAndCleanAttachments(MailMessage mailMessage) {
        try {
            this.mailSender.send(mailMessage.preparator());
        } catch (Exception e) {
            log.error("Send Mail to " + mailMessage.getToEmail() + " failed", e);
            throw e;
        } finally {
            Map<String, File> attachments = mailMessage.getAttachments();
            if (attachments != null && !attachments.isEmpty()) {
                attachments.forEach((key, file) -> {
                    if (!FileUtils.deleteQuietly(file.getParentFile())) {
                        log.warn("deletion of file {} failed", file);
                    }
                });
            }
        }

    }

    private boolean isDevEnv() {
        return Arrays.asList(env.getActiveProfiles()).contains(DEVELOPMENT) ||
                Arrays.asList(env.getActiveProfiles()).contains(INTEGRATION) ||
                Arrays.asList(env.getActiveProfiles()).contains(STAGING) ||
                Arrays.asList(env.getActiveProfiles()).contains(HOTFIX);
    }

    private String checkBcc(MailTemplate template) {
        switch (template) {
            case COMMERCIAL_NEW_PLAN_INVOICE:
            case COMMERCIAL_MONTHLY_PRODUCT_INVOICE:
            case COMMERCIAL_MONTHLY_PRODUCT_INVOICE_CANCELLATION:
            case INVOICE_BOOKING:
                return internalInvoiceLogging;
            default:
                break;
        }

        return null;
    }
}
