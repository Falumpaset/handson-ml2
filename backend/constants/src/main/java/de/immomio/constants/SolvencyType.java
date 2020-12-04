package de.immomio.constants;

import java.util.Arrays;

/**
 * @author Maik Kingma
 */

public enum SolvencyType implements ConstantEnum {

    USERPROFILE(0, "userprofile"),
    FLAT_APPLICATION(1, "flat_application");

    public int id;

    public String key;

    SolvencyType(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public static SolvencyType byId(int id) {
        return Arrays.stream(values()).filter(ft -> ft.id == id).findFirst().orElse(null);
    }

    @Override
    public String key() {
        return key;
    }
}
