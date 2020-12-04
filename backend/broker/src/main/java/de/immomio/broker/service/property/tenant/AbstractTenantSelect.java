package de.immomio.broker.service.property.tenant;

import de.immomio.data.base.type.property.TenantSelectType;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;

public abstract class AbstractTenantSelect {

    private TenantSelectType tenantSelectType;

    public AbstractTenantSelect(TenantSelectType tenantSelectType) {
        this.tenantSelectType = tenantSelectType;
    }

    public boolean isApplicable(TenantSelectType selectType) {
        return selectType == this.tenantSelectType;
    }

    public abstract boolean run(PropertyTenant propertyTenant);
}
