package de.immomio.model.entity.admin.report;

import de.immomio.data.base.type.AbstractEnumType;

/**
 * @author Niklas Lindemann
 */
public class ReportTypeConverter extends AbstractEnumType<ReportType> {

    private static final Class<ReportType> CLAZZ = ReportType.class;

    public ReportTypeConverter() {
        super(CLAZZ);
    }
}
