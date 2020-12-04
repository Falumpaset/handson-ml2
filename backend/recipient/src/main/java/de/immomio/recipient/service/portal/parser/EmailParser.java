package de.immomio.recipient.service.portal.parser;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.ReceivedEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.springframework.mail.MailParseException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
public abstract class EmailParser {

    private final Portal portal;

    protected EmailParser(Portal portal) {
        this.portal = portal;
    }

    private static String retrieveValue(String start, String group, String end, String text, boolean quote,
                                        boolean unescape) {
        if (quote) {
            start = Pattern.quote(start);
            end = Pattern.quote(end);
        }

        String s;
        if (group == null) {
            s = String.format("%s(.+)%s", start, end);
        } else {
            s = String.format("%s(%s)%s", start, group, end);
        }

        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(text);
        String value = null;
        if (matcher.find()) {
            value = matcher.group(1).trim();
        }
        if (unescape && value != null) {
            value = StringEscapeUtils.unescapeHtml4(value);
        }
        return value;
    }

    protected static Pair<String, String> parseName(String text) {
        int idx = text.lastIndexOf(' ');

        String name;
        String firstname;

        if (idx == -1) {
            firstname = "";
            name = text;
        } else {
            firstname = text.substring(0, idx).trim();
            name = text.substring(idx + 1, text.length()).trim();
        }

        return new ImmutablePair<>(firstname, name);
    }

    protected static String retrieveValue(String start, String end, String text) {
        return retrieveValue(start, null, end, text, true, true);
    }

    protected static String retrieveValue(String start, String end, String text, boolean quote) {
        return retrieveValue(start, null, end, text, quote, true);
    }

    protected static String retrieveValue(String start, String group, String end, String text, boolean quote) {
        return retrieveValue(start, null, end, text, quote, true);
    }

    private static boolean isXML(BodyPart bp) throws IOException, MessagingException {
        InputStream inputStream = bp.getInputStream();

        try {
            XmlPullParser parser = new MXParser();
            parser.setInput(inputStream, "UTF-8");
            parser.nextToken();

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            IOUtil.close(inputStream);
        }
    }

    public abstract ListingDetail parse(Message message) throws MailParseException;

    // Check this method, because we always read last XML attached file for example and ignore other ones
    protected ReceivedEmail getContent(Object content, ReceivedEmail receivedEmail) throws IOException,
            MessagingException {
        if (content instanceof Multipart) {
            Multipart multipart = ((Multipart) content);

            for (int j = 0; j < multipart.getCount(); ++j) {
                MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(j);

                if (mimeBodyPart.getContent() instanceof Multipart) {
                    getContent(mimeBodyPart.getContent(), receivedEmail);
                } else if (mimeBodyPart.isMimeType("text/html")) {
                    InputStream inputStream = mimeBodyPart.getInputStream();
                    String text = IOUtils.toString(inputStream, "UTF-8");

                    receivedEmail.setHtml(text);
                } else if (isXML(mimeBodyPart)) {
                    InputStream inputStream = mimeBodyPart.getInputStream();
                    String text = IOUtils.toString(inputStream, "UTF-8");

                    receivedEmail.setXml(text);
                } else if (mimeBodyPart.isMimeType("text/plain")) {
                    InputStream inputStream = mimeBodyPart.getInputStream();
                    String text = IOUtils.toString(inputStream, "UTF-8");

                    receivedEmail.setPlain(text);
                }
            }
        } else if (content instanceof String) {
            receivedEmail.setPlain(content.toString());
        } else {
            log.error("No usable content found [" + content.getClass().getSimpleName() + "] -> " + portal.name());
        }

        return receivedEmail;
    }

    protected List<ReceivedEmail> getContents(Object content) throws IOException, MessagingException {
        List<ReceivedEmail> receivedEmails = new ArrayList<>();
        if (content instanceof Multipart) {
            receivedEmails.addAll(getMultipartContents((Multipart) content));
        } else if (content instanceof String) {
            ReceivedEmail receivedEmail = new ReceivedEmail();
            receivedEmail.setPlain(content.toString());
            receivedEmails.add(receivedEmail);
        } else {
            log.error("No usable content found [" + content.getClass().getSimpleName() + "] -> " + portal.name());
        }

        return receivedEmails;
    }

    private List<ReceivedEmail> getMultipartContents(Multipart multipart) throws IOException, MessagingException {
        List<ReceivedEmail> receivedEmails = new ArrayList<>();
        for (int j = 0; j < multipart.getCount(); ++j) {
            ReceivedEmail receivedEmail = new ReceivedEmail();

            MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(j);
            if (mimeBodyPart.getContent() instanceof Multipart) {
                receivedEmails.addAll(getContents(mimeBodyPart.getContent()));
            } else if (mimeBodyPart.isMimeType("text/html")) {
                InputStream inputStream = mimeBodyPart.getInputStream();
                String text;
                text = extractText(inputStream);
                receivedEmail.setHtml(text);
                receivedEmails.add(receivedEmail);
            } else if (isXML(mimeBodyPart)) {
                InputStream inputStream = mimeBodyPart.getInputStream();
                String text = extractText(inputStream);

                receivedEmail.setXml(text);
                receivedEmails.add(receivedEmail);
            } else if (mimeBodyPart.isMimeType("text/plain")) {
                InputStream inputStream = mimeBodyPart.getInputStream();
                String text =  extractText(inputStream);

                receivedEmail.setPlain(text);
                receivedEmails.add(receivedEmail);
            }
        }

        return receivedEmails;
    }

    protected String extractText(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    public boolean isApplicableSender(String sender) {
        if (sender != null) {
            sender = sender.trim();
            return sender.toLowerCase().contains(portal.getParserAddress().toLowerCase());
        } else {
            return false;
        }
    }

    public Portal getPortal() {
        return portal;
    }
}
