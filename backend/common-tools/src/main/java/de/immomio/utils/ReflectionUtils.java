/**
 *
 */
package de.immomio.utils;

import de.immomio.data.shared.bean.common.S3File;
import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public final class ReflectionUtils {

    public static final List<Class<?>> BASE_TYPES = Arrays.asList(String.class, Boolean.class, Character.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, Enum.class,
            Date.class, S3File.class);

    private ReflectionUtils() {

    }

    public static void trimFields(Object value) {
        if (value == null) {
            return;
        }
        Field[] declaredFields = value.getClass().getDeclaredFields();
        Arrays.stream(declaredFields).forEach(field -> {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(value);
                if (fieldValue == null) {
                    return;
                }
                if (fieldValue instanceof String) {
                    String stringValue = (String) fieldValue;
                    field.set(value, stringValue.trim());
                }
                if (!isBaseType(fieldValue)) {
                    trimFields(fieldValue);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

    }

    public static boolean isBaseType(Object value) {
        return value != null && (
                BASE_TYPES.contains(value.getClass())
                        || value instanceof Comparable
                        || value instanceof XMLGregorianCalendar
                        || (value instanceof Collection<?> && !(value instanceof List<?>)));
    }

    public static List<Field> getAllFields(Class<?> type) {
        return getAllFields(new ArrayList<>(), type);
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.stream(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static Object getFieldValue(Field field, Object object) throws IllegalAccessException {
        if (object == null) {
            return null;
        }

        return field.get(object);
    }
}
