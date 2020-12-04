package de.immomio.broker.queue.listener.additionalLogic.property.afterDeactivate;

import de.immomio.broker.queue.listener.additionalLogic.property.AbstractAdditionalLogic;
import de.immomio.data.base.type.property.PropertyWriteProtection;
import de.immomio.data.landlord.entity.property.Property;

public class ProtectAfterDeactivate extends AbstractAdditionalLogic {

    @Override
    public boolean doLogic(Property property) {
        if (property == null) {
            return false;
        }

        if (property.getWriteProtection() == PropertyWriteProtection.IMPORT_PROTECTED) {
            return true;
        }

        property.setWriteProtection(PropertyWriteProtection.IMPORT_PROTECTED);

        return true;
    }

}
