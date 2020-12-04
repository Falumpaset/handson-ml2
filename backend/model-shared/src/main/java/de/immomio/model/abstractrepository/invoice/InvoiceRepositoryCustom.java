package de.immomio.model.abstractrepository.invoice;

import de.immomio.data.base.entity.invoice.AbstractInvoice;

public interface InvoiceRepositoryCustom<I extends AbstractInvoice> {
    I customSave(I invoice);

    I customFindOne(Long id);
}
