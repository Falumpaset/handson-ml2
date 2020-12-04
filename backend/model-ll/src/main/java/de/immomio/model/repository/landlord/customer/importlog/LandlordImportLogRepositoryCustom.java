/**
 *
 */
package de.immomio.model.repository.landlord.customer.importlog;

import de.immomio.data.landlord.entity.importlog.ImportLog;
import org.springframework.data.repository.query.Param;

/**
 * @author Bastian Bliemeister.
 */
public interface LandlordImportLogRepositoryCustom {

    ImportLog customSave(ImportLog importLog);

    ImportLog customFindOne(@Param("id") Long id);

}
