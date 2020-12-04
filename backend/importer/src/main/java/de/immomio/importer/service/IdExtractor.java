package de.immomio.importer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Maik Kingma.
 */

@Slf4j
@Service
public class IdExtractor {

    public Long extractIDFromResource(EntityModel<?> resource) {
        Long id = null;
        Optional<Link> linkOpt = getId(resource);

        if (linkOpt.isPresent()) {
            String href = linkOpt.get().getHref();
            String[] sa = href.split("/");
            try {
                id = Long.parseLong(sa[sa.length - 1]);
            } catch (NumberFormatException e) {
                log.error("Error extracting ID -> " + href);
            }
        } else {
            log.error("Self link is not present");
        }

        return id;
    }

    public Optional<Link> getId(EntityModel<?> resource) {
        return resource.getLink(IanaLinkRelations.SELF);
    }
}
