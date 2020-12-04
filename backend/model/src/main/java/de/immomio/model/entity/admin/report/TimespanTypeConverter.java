package de.immomio.model.entity.admin.report;

import de.immomio.data.base.type.AbstractEnumType;

/**
 * @author Niklas Lindemann
 */
public class TimespanTypeConverter extends AbstractEnumType<TimespanType> {

    private static final Class<TimespanType> CLAZZ = TimespanType.class;

    public TimespanTypeConverter() {
        super(CLAZZ);
    }
}
