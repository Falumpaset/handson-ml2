package de.immomio.model.entity.admin.user;

import de.immomio.data.base.type.AbstractEnumType;

public class AdminUserTypeConverter extends AbstractEnumType<AdminUserType> {

    private static final Class<AdminUserType> CLAZZ = AdminUserType.class;

    public AdminUserTypeConverter() {
        super(CLAZZ);
    }
}
