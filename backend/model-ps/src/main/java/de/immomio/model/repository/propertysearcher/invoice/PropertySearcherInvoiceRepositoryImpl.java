package de.immomio.model.repository.propertysearcher.invoice;

import de.immomio.data.propertysearcher.entity.invoice.PropertySearcherInvoice;
import de.immomio.model.abstractrepository.invoice.InvoiceRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherInvoiceRepositoryImpl implements InvoiceRepositoryCustom<PropertySearcherInvoice> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherInvoiceRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherInvoice.class);
    }

    @Override
    @Transactional
    public PropertySearcherInvoice customSave(PropertySearcherInvoice invoice) {
        if (invoice.isNew()) {
            entityManager.persist(invoice);
        } else {
            entityManager.merge(invoice);
        }

        return invoice;
    }

    @Override
    public PropertySearcherInvoice customFindOne(Long id) {
        return entityManager.find(PropertySearcherInvoice.class, id);
    }
}
