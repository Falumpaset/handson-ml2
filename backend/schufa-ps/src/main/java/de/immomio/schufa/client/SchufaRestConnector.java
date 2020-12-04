package de.immomio.schufa.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.schufa.util.KeyStoreHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;

/**
 * @author Johannes Hiemer.
 */

@Slf4j
@Component
public class SchufaRestConnector {

    private static final String KEY_STORE_PASS = "abc";
    private static final String AUTHORIZATION = "Authorization";
    private RestTemplate restTemplate = new RestTemplate();

    private KeyStoreHandler keyStoreHandler;

    @Value("${schufa.rest.host}")
    private String schufaHost;

    @Value("${schufa.rest.token}")
    private String token;

    @Value("${schufa.certificate.location}")
    private String certificatePath;

    @Value("${schufa.privatekey.password}")
    private String privateKeyPassword;

    @Value("${schufa.privatekey.location}")
    private String privateKeyLocation;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    public SchufaRestConnector(KeyStoreHandler keyStoreHandler) {
        this.keyStoreHandler = keyStoreHandler;
    }

    @PostConstruct
    private void init() throws Exception {
        headers.set(AUTHORIZATION, token);
        headers.set("Content-Type", "application/json");
        configureRestTemplate();
    }

    private void configureRestTemplate() throws Exception {

        String cert = IOUtils.toString(getClass().getResourceAsStream(certificatePath), StandardCharsets.UTF_8);
        String privateKey = IOUtils.toString(getClass().getResourceAsStream(privateKeyLocation), StandardCharsets.UTF_8);
        SSLContext sslContext = new SSLContextBuilder()
                .loadKeyMaterial(keyStoreHandler
                        .getKeyStore(cert, privateKey, privateKeyPassword, KEY_STORE_PASS), privateKeyPassword.toCharArray())
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate =  new RestTemplate(factory);
        this.restTemplate.setErrorHandler(new SchufaErrorHandler());
    }

    public <I, O> ResponseEntity runRequest(String url, I payload, Class<O> responseType) {
        String callUrl = schufaHost + url;
        log.info("start request " + callUrl);
        HttpEntity<I> httpEntity = new HttpEntity<>(payload, headers);

        try {
            log.info("with payload: ");
            log.info(new ObjectMapper().writeValueAsString(httpEntity.getBody()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ResponseEntity<O> response = restTemplate.exchange(callUrl, HttpMethod.POST, httpEntity, responseType);

        log.info("finished request " + response.getStatusCode().toString() + " " + response.getBody());

        return response;
    }
}
