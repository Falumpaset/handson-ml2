package de.immomio.model.repository.unsecured.shortUrl;

import de.immomio.data.shared.entity.shortUrl.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(exported = false)
public interface BaseShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    ShortUrl findByExternalId(String externalId);

    List<ShortUrl> findByRedirectUrl(String redirectUrl);

    List<ShortUrl> findByExpiresLessThan(Date expires);
}
