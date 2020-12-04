package de.immomio.cloud.service.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Service
public class GoogleSheetsService {

    private static final String USER_ENTERED = "USER_ENTERED";
    private static final String CERTS_GOOGLE_SHEETS_CERT_P_12 = "/certs/google-sheets-cert.p12";
    private Sheets sheetsService;

    private static final String APPLICATION_NAME = "Immomio User Monitoring";

    @Value("${google.sheets.account_id}")
    private String serviceAccountId;

    @PostConstruct
    public void init() {
        try {
            InputStream certFile = GoogleSheetsService.class.getResourceAsStream(CERTS_GOOGLE_SHEETS_CERT_P_12);

            List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);

            GoogleCredential credentials = new GoogleCredential.Builder()
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(JacksonFactory.getDefaultInstance())
                    .setServiceAccountId(serviceAccountId)
                    .setServiceAccountPrivateKeyFromP12File(certFile).setServiceAccountScopes(scopes).build();

            this.sheetsService = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            credentials.refreshToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void writeValue(String spreadsheetId, String range, String... values) throws IOException {
        ValueRange appendBody = new ValueRange()
                .setValues(Collections.singletonList(
                        Arrays.asList(values)));

        sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, appendBody)
                .setValueInputOption(USER_ENTERED)
                .setIncludeValuesInResponse(true)
                .execute();
    }
}
