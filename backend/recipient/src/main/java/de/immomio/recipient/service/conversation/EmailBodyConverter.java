package de.immomio.recipient.service.conversation;

import de.immomio.recipient.service.indexing.RecipientIndexingService;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.simplejavamail.api.email.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fabian Beck.
 */

@Slf4j
@Service
public class EmailBodyConverter {

    public static final String REGEX_NEW_LINE_DOUBLE_BACKLASH = "\\\\n";
    public static final String REGEX_NO_BREAK_SPACE = "&nbsp;";
    public static final String REGEX_NEW_LINE_WITH_WHITE_SPACES = "[^\\S\\n\\r]*\\R[^\\S\\n\\r]*";
    public static final String REGEX_EMAIL_PARSER = "((.*\\R?(?!.*<\\R?(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\\R?>.*:?))*)";

    public static final String CSS_QUERY_BR = "br";
    public static final String CSS_QUERY_P = "p";
    public static final String CSS_QUERY_DIV = "div";

    public static final String WHITE_SPACE = " ";

    public static final String NEW_LINE_SPACE = "\n";
    public static final String NEW_LINE_DOUBLE_BACKSLASH = "\\n";

    private final RecipientIndexingService recipientIndexingService;

    @Autowired
    public EmailBodyConverter(RecipientIndexingService recipientIndexingService) {
        this.recipientIndexingService = recipientIndexingService;
    }

    public String extractMessage(Email email) {
        String content = null;
        if (email.getHTMLText() != null) {
            content = extractPlainEmailFromHtml(email);
        }
        if (content == null) {
            content = email.getPlainText();
        }

        if (content == null) {
            content = Strings.EMPTY;
        }

        content = content.replaceAll(REGEX_NEW_LINE_WITH_WHITE_SPACES, NEW_LINE_SPACE);

        Pattern r = Pattern.compile(REGEX_EMAIL_PARSER);
        Matcher m = r.matcher(content);

        if (m.find()) {
            return m.group(1).strip();
        } else {
            recipientIndexingService.proposalAccepted(email);
            return content;
        }
    }

    private String extractPlainEmailFromHtml(Email email) {
        String content;
        Document emailDoc = Jsoup.parse(email.getHTMLText());
        emailDoc.outputSettings(new Document.OutputSettings().prettyPrint(false));
        emailDoc.select(CSS_QUERY_BR).after(NEW_LINE_DOUBLE_BACKSLASH);
        emailDoc.select(CSS_QUERY_P).before(NEW_LINE_DOUBLE_BACKSLASH);
        emailDoc.select(CSS_QUERY_DIV).before(WHITE_SPACE);
        String htmlEmail = emailDoc.html().replaceAll(REGEX_NEW_LINE_DOUBLE_BACKLASH, NEW_LINE_SPACE);

        content = Parser.unescapeEntities(Jsoup.clean(htmlEmail, Strings.EMPTY, Whitelist.none(),
                new Document.OutputSettings().prettyPrint(false)).replaceAll(REGEX_NO_BREAK_SPACE, Strings.EMPTY),
                false);
        return content;
    }

}
