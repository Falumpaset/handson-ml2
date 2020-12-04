package de.immomio.service.shortUrl;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.shared.entity.shortUrl.ShortUrl;
import de.immomio.data.shared.entity.shortUrl.ShortUrlType;
import de.immomio.model.repository.unsecured.shortUrl.BaseShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class ShortUrlService {

    public static final String TOKENPARAMETER = "token";
    private static final String PORTAL = "portal";
    private final static int MINIDLENGTH = 5;

    private final static int MAXIDLENGTH = 8;

    @Value("${shortUrl.protocol}")
    private String protocol;

    @Value("${shortUrl.host}")
    private String host;

    @Value("${shortUrl.path}")
    private String path;

    @Value("${shortUrl.protocol}")
    private String tenantProtocol;

    @Value("${shortUrl.host}")
    private String tenantHost;

    @Value("${shortUrl.path}")
    private String tenantPath;

    @Value("${shortUrl.redirect.error.default}")
    private String defaultErrorRedirect;

    @Autowired
    private BaseShortUrlRepository shortUrlRepository;

    public URL createUrl(String url) {
        return createUrl(url, false);
    }

    public URL createUrl(String url, boolean redirectWithToken) {
        return createUrl(url, null, null, redirectWithToken);
    }

    public URL createUrl(String url, Map<String, String> properties) {
        return createUrl(url, properties, false);
    }

    public URL createUrl(String url, Map<String, String> properties, boolean redirectWithToken) {
        return createUrl(url, properties, null, redirectWithToken);
    }

    public URL createUrl(String url, Date expires) {
        return createUrl(url, expires, false);
    }

    public URL createUrl(String url, Date expires, boolean redirectWithToken) {
        return createUrl(url, null, expires, redirectWithToken);
    }

    public URL createUrl(String url, Map<String, String> properties, Date expires) {
        return createUrl(url, properties, expires, false);
    }

    public URL createUrl(String url, Map<String, String> properties, Date expires, boolean redirectWithToken) {
        return create(url, properties, expires, redirectWithToken, protocol, host, path, ShortUrlType.LANDLORD);
    }

    public URL createTenantUrl(String url) {
        return createTenantUrl(url, false);
    }

    public URL createTenantUrl(String url, boolean redirectWithToken) {
        return createTenantUrl(url, null, null, false);
    }

    public URL createTenantUrl(String url, Map<String, String> properties) {
        return createTenantUrl(url, properties, false);
    }

    public URL createTenantUrl(String url, Map<String, String> properties, boolean redirectWithToken) {
        return createTenantUrl(url, properties, null, redirectWithToken);
    }

    public URL createTenantUrl(String url, Date expires) {
        return createTenantUrl(url, expires, false);
    }

    public URL createTenantUrl(String url, Date expires, boolean redirectWithToken) {
        return createTenantUrl(url, null, expires, redirectWithToken);
    }

    public URL createTenantUrl(String url, Map<String, String> properties, Date expires) {
        return createTenantUrl(url, properties, expires, false);
    }

    public URL createTenantUrl(String url, Map<String, String> properties, Date expires, boolean redirectWithToken) {
        return create(url, properties, expires, redirectWithToken, tenantProtocol, tenantHost, tenantPath,
                ShortUrlType.TENANT);
    }

    private ShortUrl getExistingUrl(String url, Map<String, String> properties, Date expires,
                                    boolean redirectWithToken) {
        ShortUrl shortUrl = null;

        List<ShortUrl> list = shortUrlRepository.findByRedirectUrl(url);

        for (ShortUrl tmpUrl : list) {
            if (redirectWithToken != tmpUrl.isRedirectWithToken()) {
                continue;
            } else if ((tmpUrl.getProperties() == null || tmpUrl.getProperties().size() == 0) &&
                    (properties == null || properties.size() == 0)) {
                shortUrl = tmpUrl;
                break;
            } else if (tmpUrl.getProperties() == null || properties == null) {
                continue;
            } else if (tmpUrl.getProperties().size() != properties.size()) {
                continue;
            }

            Map<String, String> tmpProperties = tmpUrl.getProperties();
            boolean match = true;
            for (String key : properties.keySet()) {
                if (!tmpProperties.containsKey(key)) {
                    match = false;
                    break;
                }

                if (Objects.equals(properties.get(key), tmpProperties.get(key))) {
                    continue;
                }

                match = false;
                break;
            }

            if (match) {
                shortUrl = tmpUrl;
                break;
            }
        }

        if (shortUrl != null &&
                (expires == null || shortUrl.getExpires() == null || expires.after(shortUrl.getExpires()))) {
            shortUrl.setExpires(expires);
            shortUrlRepository.save(shortUrl);
        }

        return shortUrl;
    }

    private URL create(String url, Map<String, String> properties, Date expires, boolean redirectWithToken,
                       String protocol, String host, String path, ShortUrlType type) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("url may not be null");
        }

        ShortUrl shortUrl;

        shortUrl = getExistingUrl(url, properties, expires, redirectWithToken);

        if (shortUrl == null) {
            shortUrl = new ShortUrl();
            shortUrl.setExternalId(generateExternalId(type));

            shortUrl.setCalled(0);

            shortUrl.setRedirectUrl(url);
            shortUrl.setExpires(expires);
            shortUrl.setProperties(properties);

            shortUrl.setRedirectWithToken(redirectWithToken);

            shortUrlRepository.save(shortUrl);
        }

        URL retUrl = null;
        try {
            retUrl = new URL(protocol, host, path + shortUrl.getExternalId());
        } catch (MalformedURLException e) {
            log.error("ERROR creating ShortUrl for " + url, e);
        }

        return retUrl;
    }

    public URL getErrorRedirect(ShortUrl shortUrl) {
        return getErrorRedirect(shortUrl.getExternalId());
    }

    public URL getErrorRedirect(String externalId) {
        return getErrorRedirect(ShortUrlType.getUrlType(externalId));
    }

    public URL getErrorRedirect(ShortUrlType type) {
        if (type == null) {
            type = ShortUrlType.DEFAULT;
        }

        String url;
        switch (type) {
            case TENANT:
            case LANDLORD:
            case DEFAULT:
            default:
                url = defaultErrorRedirect;
        }

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            log.error("can't convert to URL ...", e);
            return null;
        }
    }

    public URL getRedirect(String externalId) {
        return getRedirect(externalId, true);
    }

    public URL getRedirect(String externalId, boolean increment) {
        if (externalId == null || externalId.trim().isEmpty()) {
            log.info("externalId is null or empty -> redirect to default location ...");
            return getErrorRedirect(ShortUrlType.DEFAULT);
        }

        ShortUrl shortUrl = shortUrlRepository.findByExternalId(externalId);

        if (shortUrl == null) {
            log.info("can't find externalId [" + externalId + "] -> redirect to default location ...");
            return getErrorRedirect(externalId);
        }

        URI redirectUrl;
        try {
            redirectUrl = new URI(shortUrl.getRedirectUrl());
        } catch (URISyntaxException e) {
            log.error("can't convert externalId [" + externalId + "] to URI -> redirect to default location ...", e);
            return getErrorRedirect(ShortUrlType.DEFAULT);
        }

        if (shortUrl.isRedirectWithToken()) {
            StringBuilder query = new StringBuilder();

            if (redirectUrl.getQuery() != null && !redirectUrl.getQuery().trim().isEmpty()) {
                query.append(redirectUrl.getQuery());
                query.append("&");
            }

            query.append(TOKENPARAMETER);
            query.append("=");
            query.append(shortUrl.getExternalId());

            try {
                redirectUrl = new URI(redirectUrl.getScheme(), redirectUrl.getUserInfo(), redirectUrl.getHost(),
                        redirectUrl.getPort(), redirectUrl.getPath(), query.toString(), redirectUrl.getRawFragment());
            } catch (URISyntaxException e) {
                log.error("can't convert externalId [" + externalId + "] to URL -> redirect to default location ...",
                        e);
                return getErrorRedirect(ShortUrlType.DEFAULT);
            }
        }

        if (increment) {
            shortUrl.setCalled(shortUrl.getCalled() + 1);
            shortUrlRepository.save(shortUrl);
        }

        try {
            return redirectUrl.toURL();
        } catch (MalformedURLException e) {
            log.error("can't convert externalId [" + externalId + "] to URL -> redirect to default location ...", e);
            return getErrorRedirect(ShortUrlType.DEFAULT);
        }
    }

    public Portal getPortalFromShortUrl(String shortUrlToken) {
        ShortUrl shortUrl = shortUrlRepository.findByExternalId(shortUrlToken);
        if (shortUrl == null) {
            return null;
        }
        String portalKey = shortUrl.getProperties().get(PORTAL);
        if (portalKey == null) {
            return null;
        }
        try {
            return Portal.valueOf(portalKey);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private String generateExternalId(ShortUrlType type) {
        int length = new Random().nextInt(MAXIDLENGTH - MINIDLENGTH + 1) + MINIDLENGTH;

        String externalId = RandomStringUtils.random(length, true, true);

        return externalId;
    }

}
