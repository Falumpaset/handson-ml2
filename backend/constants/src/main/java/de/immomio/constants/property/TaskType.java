package de.immomio.constants.property;

/**
 * @author Bastian Bliemeister.
 */
public enum TaskType {

    ACTIVATE(0),
    DEACTIVATE(1),
    UPDATE(2),
    DELETE(3),
    STATIC(4);

    public int id;

    TaskType(int id) {
        this.id = id;
    }

}
