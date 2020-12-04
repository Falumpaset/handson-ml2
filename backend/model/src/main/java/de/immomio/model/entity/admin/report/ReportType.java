package de.immomio.model.entity.admin.report;

/**
 * @author Niklas Lindemann
 */
public enum ReportType {
    LANDLORD, PROPERTYSEARCHER, TOTAL;

    @Override
    public String toString() {
        return this.name();
    }
}
