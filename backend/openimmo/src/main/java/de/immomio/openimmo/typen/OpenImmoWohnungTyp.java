package de.immomio.openimmo.typen;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck
 */

public enum OpenImmoWohnungTyp {
    ERDGESCHOSS("ERDGESCHOSS"), SOUTERRAIN("SOUTERRAIN"), LOFT_STUDIO_ATELIER("LOFT-STUDIO-ATELIER"), MAISONETTE(
            "MAISONETTE"), PENTHOUSE("PENTHOUSE"), ETAGE("ETAGE"), DACHGESCHOSS("DACHGESCHOSS"), TERRASSEN(
            "TERRASSEN"), GALERIE("GALERIE"), APARTMENT("APARTMENT"), KEINE_ANGABE("KEINE_ANGABE");

    private static final Map<String, OpenImmoWohnungTyp> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(OpenImmoWohnungTyp::key, Function.identity()));

    private final String key;

    OpenImmoWohnungTyp(String key) {
        this.key = key;
    }

    public static OpenImmoWohnungTyp getByKey(String key) {
        return lookup.getOrDefault(key, null);
    }

    public String key() {
        return key;
    }
}
