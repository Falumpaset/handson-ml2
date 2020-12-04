package de.immomio.itp.client;

import de.immomio.itp.config.ItpWebClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Andreas Hansen
 */
@Slf4j
@Component
public class ItpRestConnector {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private WebClient webClient;

    private ItpWebClientConfiguration itpWebClientConfiguration;

    public ItpRestConnector(
            WebClient.Builder webClientBuilder,
            ItpWebClientConfiguration itpWebClientConfiguration
    ) {
        this.itpWebClientConfiguration = itpWebClientConfiguration;
        configureWebClient(webClientBuilder);
    }

    private void configureWebClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder
                .baseUrl(itpWebClientConfiguration.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(AUTHORIZATION, BEARER + itpWebClientConfiguration.getToken())
                .build();
    }

    public <I, O> Mono<O> runGetRequest(String url, Class<O> responseType) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <I, O> Mono<O> runPostRequest(String url, I payload, Class<O> responseType) {
        return webClient
                .post()
                .uri(url)
                .body(Mono.just(payload), payload.getClass())
                .retrieve()
                .bodyToMono(responseType);
    }

}
