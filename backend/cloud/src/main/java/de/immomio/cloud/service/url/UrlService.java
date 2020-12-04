package de.immomio.cloud.service.url;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class UrlService {

    @Value("${base.propertysearcher.applicationpath}")
    private String applicationPath;

    @Value("${base.propertysearcher.url}")
    private String propertySearcherUrl;

    @PostConstruct
    public void init() {
    }

    public String getApplicationLink(Long propertyId) {
        StringBuilder sb = new StringBuilder(propertySearcherUrl);

        if (applicationPath != null) {
            sb.append(String.format(applicationPath, propertyId));
        }

        return sb.toString();
    }
}
