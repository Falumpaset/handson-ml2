package de.immomio.utils.compare;

import de.immomio.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public final class DiffUtils {

    public static final String SEPARATOR = ".";

    private DiffUtils() {
    }

    public static boolean deepEquals(Object p1, Object p2) {
        List<CompareBean> compareBeans = new ArrayList<>();

        diffObjects(p1, p2, compareBeans, "", null);
        return compareBeans.isEmpty();
    }

    public static List<CompareBean> getDifferences(Object p1, Object p2) {
        List<CompareBean> compareBeans = new ArrayList<>();
        diffObjects(p1, p2, compareBeans, "", null);
        return compareBeans;
    }

    private static void diffObjects(Object p1, Object p2, List<CompareBean> compareBeans, String parent, Class<?> parentClass) {
        if ((p1 == null && p2 == null) || ReflectionUtils.isBaseType(p1) || ReflectionUtils.isBaseType(p2)) {
            if (!Objects.equals(p1, p2)) {
                compareBeans.add(new CompareBean(StringUtils.strip(parent, SEPARATOR), p1, p2, parentClass));
            }
            return;
        }

        Object objectToGetFields = p1 != null ? p1 : p2;
        List<Field> fields = ReflectionUtils.getAllFields(objectToGetFields.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object valueOne = ReflectionUtils.getFieldValue(field, p1);
                Object valueTwo = ReflectionUtils.getFieldValue(field, p2);

                if (valueOne instanceof List<?> && valueTwo instanceof List<?>) {
                    diffList((List<?>) valueOne, (List<?>) valueTwo, compareBeans,
                            StringUtils.strip(parent + SEPARATOR + field.getName(), SEPARATOR), objectToGetFields.getClass());
                } else {
                    diffObjects(valueOne, valueTwo, compareBeans, parent + SEPARATOR + field.getName(), objectToGetFields.getClass());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return;
            }
        }
    }

    private static void diffList(List<?> l1, List<?> l2, List<CompareBean> compareBeans, String parent, Class<?> parentClass) {
        if (l1.size() != l2.size()) {
            compareBeans.add(new CompareBean(StringUtils.strip(parent, SEPARATOR), l1.size(), l2.size(), parentClass));
        }

        for (int i = 0; i < l1.size(); i++) {
            Object o2 = null;

            if (i < l2.size()) {
                o2 = l2.get(i);
            }

            diffObjects(l1.get(i), o2, compareBeans, StringUtils.strip(parent + SEPARATOR + i, SEPARATOR), l1.getClass());
        }

    }

}
