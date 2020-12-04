package de.immomio.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Niklas Lindemann
 */
public class ListUtils {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new HashMap<>();
        return value -> seen.putIfAbsent(keyExtractor.apply(value), Boolean.TRUE) == null;
    }
}
