package de.immomio.schufa.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public class SchufaErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("schufa request failed " + IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        //always pass the error to the client
        return false;
    }
}
