package de.immomio.constants;

public enum Group {

    ADMIN(20),
    EMPLOYEE(21),
    OWNER(22),
    TENANT(23),
    COMMERCIAL(24),
    HOTLINE(25);

    public final long id;

    Group(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
