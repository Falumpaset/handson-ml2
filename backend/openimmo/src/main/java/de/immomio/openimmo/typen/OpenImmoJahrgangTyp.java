package de.immomio.openimmo.typen;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck
 */

public enum OpenImmoJahrgangTyp {
    TWOTHOUSANDEIGHT("2008"), TWOTHOUSANDFOURTEEN("2014"), OHNE("ohne"), NICHT_NOETIG("nicht_noetig");

    private final String key;

    OpenImmoJahrgangTyp(String key) {
        this.key = key;
    }


    private static final Map<String, OpenImmoJahrgangTyp> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(OpenImmoJahrgangTyp::key, Function.identity()));


    public static OpenImmoJahrgangTyp getByKey(String key) {
        return lookup.getOrDefault(key, null);
    }

    public String key() {
        return key;
    }
}
