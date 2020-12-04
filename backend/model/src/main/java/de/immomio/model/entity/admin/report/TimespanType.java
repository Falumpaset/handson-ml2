package de.immomio.model.entity.admin.report;

/**
 * @author Niklas Lindemann
 */
public enum TimespanType {
    MONTHLY, WEEKLY, CUSTOM;

    @Override
    public String toString() {
        return this.name();
    }
}
