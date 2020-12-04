package de.immomio.constants.customer;

import de.immomio.constants.ConstantEnum;

public enum Title implements ConstantEnum {
    NONE("none"),
    DR("dr"),
    DR_DR("dr_dr"),
    PROF("professor"),
    PROF_DR("professor_dr");

    private String key;

    Title(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
