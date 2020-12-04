package de.immomio.model.repository.landlord.invoice;

import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.model.abstractrepository.invoice.InvoiceRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class LandlordInvoiceRepositoryImpl implements InvoiceRepositoryCustom<LandlordInvoice> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordInvoiceRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordInvoice.class);
    }

    @Override
    @Transactional
    public LandlordInvoice customSave(LandlordInvoice invoice) {
        if (invoice.isNew()) {
            entityManager.persist(invoice);
        } else {
            entityManager.merge(invoice);
        }

        return invoice;
    }

    @Override
    public LandlordInvoice customFindOne(Long id) {
        return entityManager.find(LandlordInvoice.class, id);
    }
}
