package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.shared.entity.shortUrl.ShortUrl;
import de.immomio.model.repository.unsecured.shortUrl.BaseShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Bastian Bliemeister.
 */
@Slf4j
@Component
public class DeleteShortUrlsTask extends BaseTask {

    @Autowired
    private BaseShortUrlRepository shortUrlRepository;

    @Override
    public boolean run() {
        log.info("Starting to delete expired ShortUrl's ...");
        List<ShortUrl> shortUrls = shortUrlRepository.findByExpiresLessThan(new Date());

        for (ShortUrl url : shortUrls) {
            shortUrlRepository.delete(url);
            log.info(url.getExternalId() + " deleted [" + url.getRedirectUrl() + "]");
        }

        return true;
    }

}
