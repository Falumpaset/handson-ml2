package de.immomio.reporting.config;

import io.pivotal.cfenv.core.CfEnv;
import io.pivotal.cfenv.core.CfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Configuration
public class CustomElasticsearchRepositoryConfig {

    @Configuration
    @Profile({"default", "development", "test-api"})
    public static class Default {

        @Value("${elasticsearch.host}")
        private String host;

        @Value("${elasticsearch.rest.port}")
        private Integer restPort;

        @Value("${elasticsearch.nodes.port}")
        private Integer nodesPort;

        @Bean
        public RestHighLevelClient client() {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(host, restPort, "http")));

            return client;
        }
    }

    @Configuration
    @Profile("cloud")
    public static class CloudConfig {

        private static final String SSL = "SSL";

        @Value("${elasticsearch.scheme}")
        private String scheme;

        @Value("${service.elasticsearch}")
        private String serviceName;

        @Bean
        public RestHighLevelClient client() throws NoSuchAlgorithmException, KeyManagementException {
            CfEnv cfEnv = new CfEnv();
            CfService service = cfEnv.findServiceByLabel(serviceName);

            HttpHost httpHost = new HttpHost(service.getCredentials().getHost(), Integer.parseInt(service.getCredentials().getPort()), scheme);

            final CredentialsProvider credentialsProvider =
                    new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(service.getCredentials().getUsername(), service.getCredentials().getPassword()));

            TrustManager trustManager = getTrustManager();

            SSLContext sslContext = SSLContext.getInstance(SSL);
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(httpHost)
                            .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                                    .setDefaultCredentialsProvider(credentialsProvider)
                                    .setSSLContext(sslContext)));

            return client;
        }

        private TrustManager getTrustManager() {
            return new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
        }

    }
}
