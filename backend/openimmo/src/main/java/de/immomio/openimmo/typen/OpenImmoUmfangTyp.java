package de.immomio.openimmo.typen;

/**
 * @author Fabian Beck
 */

public enum OpenImmoUmfangTyp {
    TEIL("TEIL"), VOLL("VOLL");

    private final String key;

    OpenImmoUmfangTyp(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
