package de.immomio.utils.criteria;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
public class CriteriaQueryUtils {

    private static final String SPLIT_PARAM = "\\.";
    private static final String JSONB_EXTRACT_PATH_TEXT = "jsonb_extract_path_text";
    private static final String PERCENT = "%";
    private static final String DOT = ".";
    private static final String DOT_REGEX = "\\" + DOT;
    private static final String JSONB_FIELD_SEPARATOR = "->>";
    private static final int TWO = 2;
    private static final String EMPTY = "";

    private CriteriaQueryUtils() {
    }

    public static Predicate jsonbLike(CriteriaBuilder builder, Path<?> path, String field, String subKey, String value) {
        return builder.like(builder.lower(jsonFunction(builder, path, field, subKey)),
                PERCENT + value.toLowerCase() + PERCENT);
    }

    public static Predicate jsonbEqual(CriteriaBuilder builder, Path<?> path, String field, String subKey, Object value) {
        return builder.equal(jsonFunction(builder, path, field, subKey), value);
    }

    public static Predicate jsonbNull(CriteriaBuilder builder, Path<?> path, String field, String subKey) {
        return builder.isNull(jsonFunction(builder, path, field, subKey));
    }

    public static <Y extends Comparable<? super Y>> Predicate jsonbBetween(CriteriaBuilder builder, Path<?> path, String field, String subKey,
                                                                           Y valueFrom, Y valueTo, Class<Y> clazz) {
        Expression<? extends Y> expression = jsonFunction(builder, path, field, subKey, clazz);

        return builder.between(expression, valueFrom, valueTo);
    }

    public static Expression<String> jsonFunction(CriteriaBuilder builder, Path<?> path, String field, String subKey) {
        return jsonFunction(builder, path, field, subKey, String.class);
    }

    public static <Y extends Comparable<? super Y>> Expression<Y> jsonFunction(
            CriteriaBuilder builder, Path<?> path, String field, String subKey, Class<Y> clazz) {
        String[] spliitedKey = subKey.split(SPLIT_PARAM);

        List<Expression<?>> expressions = new ArrayList<>();
        expressions.add(path.get(field));
        expressions.addAll(Arrays.stream(spliitedKey).map(builder::literal).collect(Collectors.toList()));
        Expression[] literals = expressions.toArray(new Expression[0]);
        return builder.function(getExtractFunctionName(clazz), String.class, literals).as(clazz);
    }

    public static void populateJsonbLikePredicate(
            CriteriaBuilder builder,
            Path path,
            String field,
            String subKey,
            String value,
            List<Predicate> predicates
    ) {
        if (StringUtils.hasText(value)) {
            predicates.add(jsonbLike(builder, path, field, subKey, value));
        }
    }

    public static <Y extends Comparable> void populateJsonbBetweenPredicate(
            CriteriaBuilder builder,
            Path path,
            String field,
            String subKey,
            Y valueFrom,
            Y valueTo,
            List<Predicate> predicates,
            Class<Y> clazz

    ) {
        if (valueFrom != null && valueTo != null) {
            predicates.add(jsonbBetween(builder, path, field, subKey, valueFrom, valueTo, clazz));
        }
    }

    public static void populateJsonbEqualPredicate(
            CriteriaBuilder builder,
            Path path,
            String field,
            String subKey,
            Object value,
            List<Predicate> predicates
    ) {
        if (value != null) {
            predicates.add(jsonbEqual(builder, path, field, subKey, value));
        }
    }

    public static void populateJsonbIsNullPredicate(
            CriteriaBuilder builder,
            Path path,
            String field,
            String subKey,
            List<Predicate> predicates
    ) {
        predicates.add(jsonbNull(builder, path, field, subKey));
    }

    public static void populateJsonbInPredicate(
            CriteriaBuilder builder,
            Path path,
            String field,
            String subKey,
            Object value,
            List<Predicate> predicates
    ) {
        if (value != null) {
            predicates.add(jsonFunction(builder, path, field, subKey).in(value));
        }
    }

    public static void addOrder(Path path, List<Order> orders, Sort.Order sortOrder) {
        boolean ascending = sortOrder.getDirection() == Sort.Direction.ASC;

        orders.add(new OrderImpl(path.get(sortOrder.getProperty()), ascending));
    }

    public static List<Order> generateSortOrders(CriteriaBuilder builder, Root root, PageRequest pageRequest) {
        return generateSortOrders(builder, root, pageRequest, null);
    }

    public static List<Order> generateSortOrders(CriteriaBuilder builder, Root root, PageRequest pageRequest, Function <Sort.Order, Order> func) {
        List<Order> orders = new ArrayList<>();
        Sort sort = pageRequest.getSort();
        sort.iterator().forEachRemaining(sortOrder -> {
            if (func != null) {
                Order order = func.apply(sortOrder);
                if (order != null) {
                    orders.add(order);
                    return;
                }
            }

            boolean ascending = sortOrder.getDirection() == Sort.Direction.ASC;

            String[] splitted = sortOrder.getProperty().split(DOT_REGEX);
            if (splitted.length == TWO && !isJsonbField(sortOrder.getProperty())) {
                String propertyName = splitted[0];
                String propertyField = splitted[1];
                orders.add(new OrderImpl(root.get(propertyName).get(propertyField), ascending));
                return;
            }

            if (isPropertyField(sortOrder.getProperty()) && isJsonbField(sortOrder.getProperty())) {
                String[] fields = sortOrder.getProperty().split(DOT_REGEX);
                String[] paths = fields[1].split("\\" + JSONB_FIELD_SEPARATOR);
                orders.add(new OrderImpl(jsonFunction(builder, root.get(fields[0]), paths[0], paths[1]), ascending));
            } else if (isJsonbField(sortOrder.getProperty())) {
                String[] paths = sortOrder.getProperty().split("\\" + JSONB_FIELD_SEPARATOR);
                String fieldName = paths[0];
                String jsonElement = getJsonOrder(paths);
                orders.add(new OrderImpl(jsonFunction(builder, root, fieldName, jsonElement), ascending));
            } else {
                orders.add(new OrderImpl(root.get(sortOrder.getProperty()), ascending));
            }
        });

        return orders;
    }

    public static String getJsonOrder(String[] paths) {
        if (paths.length < TWO) {
            return EMPTY;
        }
        StringBuilder jsonElement = new StringBuilder(paths[1]);
        if (paths.length > TWO) {
            for (int i = TWO; i < paths.length; i++) {
                jsonElement.append(DOT).append(paths[i]);
            }
        }
        return jsonElement.toString();
    }

    public static boolean isJsonbField(String field) {
        return field.contains(JSONB_FIELD_SEPARATOR);
    }

    public static boolean isPropertyField(String field) {
        return field.contains(DOT);
    }

    public static <L extends Enum<L>> void addEnumListToPredicates(List<L> listToAdd, CriteriaBuilder builder, Path<L> pathToList, List<Predicate> predicates) {
        if (listToAdd != null && !listToAdd.isEmpty()) {
            CriteriaBuilder.In<L> listInRoot = builder.in(pathToList);
            listToAdd.forEach(listInRoot::value);
            predicates.add(builder.and(listInRoot));
        }
    }

    private static String getExtractFunctionName(Class<?> clazz) {
        if (clazz.equals(String.class)) {
            return JSONB_EXTRACT_PATH_TEXT;
        } else {
            return JSONB_EXTRACT_PATH_TEXT + clazz.getSimpleName();
        }
    }
}
