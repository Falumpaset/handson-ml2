/**
 *
 */
package de.immomio.model.repository.landlord.customer.importlog;

import de.immomio.data.landlord.entity.importlog.ImportLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister.
 */
public class LandlordImportLogRepositoryImpl implements LandlordImportLogRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public LandlordImportLogRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(ImportLog.class);
    }

    @Override
    @Transactional
    public ImportLog customSave(ImportLog importLog) {
        if (importLog.isNew()) {
            entityManager.persist(importLog);
        } else {
            entityManager.merge(importLog);
        }

        return importLog;
    }

    @Override
    public ImportLog customFindOne(Long id) {
        return entityManager.find(ImportLog.class, id);
    }
}
