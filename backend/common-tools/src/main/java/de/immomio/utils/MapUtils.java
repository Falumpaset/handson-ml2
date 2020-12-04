package de.immomio.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */
public class MapUtils {

    public static Map createMap(String... values) {
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < Arrays.asList(values).size(); i++) {
            if ((i + 1) % 2 == 0) {
                continue;
            }
            map.put(values[i], values[i+1]);
        }

        return map;
    }
}
