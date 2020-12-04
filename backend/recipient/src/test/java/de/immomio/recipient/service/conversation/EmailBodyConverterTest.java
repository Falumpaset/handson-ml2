package de.immomio.recipient.service.conversation;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmailBodyConverterTest {

    @InjectMocks
    private EmailBodyConverter emailBodyConverter;

    public static String getString(String file) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(EmailBodyConverterTest.class.getResourceAsStream(file), writer, StandardCharsets.UTF_8);
        return writer.toString();
    }

    @ParameterizedTest(name = "run extract Message for {arguments}")
    @ValueSource(strings = {"OutlookWindows", "GmailAndroid", "GmailWeb", "MacMailer"})
    public void successfullyExtractMessageFromHtml(String testCase) throws IOException {
        Email email = EmailBuilder.startingBlank()
                .appendTextHTML(getString("/emails/html/base/" + testCase + ".html"))
                .buildEmail();

        String receivedMessage = emailBodyConverter.extractMessage(email);

        String expectedMessage = getString("/emails/html/expected/" + testCase + ".txt");
        assertEquals(expectedMessage, receivedMessage);
    }

    @ParameterizedTest(name = "run extract Message for {arguments}")
    @ValueSource(strings = {"GmailWeb"})
    public void successfullyExtractMessageFromPlainText(String testCase) throws IOException {
        Email email = EmailBuilder.startingBlank()
                .appendText(getString("/emails/plain/base/" + testCase + ".txt"))
                .buildEmail();

        String receivedMessage = emailBodyConverter.extractMessage( email);

        String expectedMessage = getString("/emails/plain/expected/" + testCase + ".txt");
        assertEquals(expectedMessage, receivedMessage);
    }
}