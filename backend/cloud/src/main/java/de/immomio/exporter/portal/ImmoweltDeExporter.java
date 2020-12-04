/**
 *
 */
package de.immomio.exporter.portal;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.exporter.openimmo.converter.OpenImmoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Bastian Bliemeister.
 */
@Service
public class ImmoweltDeExporter extends ImmoweltExporter {

    @Autowired
    public ImmoweltDeExporter(OpenImmoConverter converter) {
        super(Portal.IMMOWELT_DE, converter);
    }

    @Override
    public List<Portal> getReleasedPortals() {
        List<Portal> list = super.getReleasedPortals();

        list.add(Portal.IMMONET_DE);

        return list;
    }
}
