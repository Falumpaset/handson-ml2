/**
 *
 */
package de.immomio.constants;

/**
 * @author Bastian Bliemeister.
 */
public enum Location implements ConstantEnum, IdentifiableEnum {

    DE(1, "de"),
    AT(2, "at");

    private final int id;

    private final String key;

    Location(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public static Location byId(int id) {
        for (Location ft : values()) {
            if (ft.id == id) {
                return ft;
            }
        }

        return null;
    }

    public static Location defaultLocation() {
        return DE;
    }

    @Override
    public String key() {
        return Location.class.getSimpleName() + "." + key;
    }

    @Override
    public int getId() {
        return id;
    }
}
