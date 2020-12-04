package de.immomio.common.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Bastian Bliemeister.
 * @NotNull
 * @Size(min = 0, max = 100) private String name;
 * <p>
 * if(name == null && AnnotationUtilities.fieldHasAnnotation(this.getClass(), NotNull.class, "name")) { throw new
 * IllegalArgumentException("name is null"); } else if(name != null && AnnotationUtilities.fieldHasAnnotation(this.getClass(),
 * Size.class, "name")) { Size size = AnnotationUtilities.getFieldAnnotation(this.getClass(), Size.class, "name");
 * <p>
 * if(size != null && name.length() > size.max()) name = name.substring(0, size.max()); else if(size != null &&
 * name.length() < size.min()) throw new IllegalArgumentException("name is to short"); }
 */

public abstract class AnnotationUtilities {

    private AnnotationUtilities() {
    }

    public static <T extends Annotation> boolean fieldHasAnnotation(Class<?> target, Class<T> anno, String name) {
        try {
            return getAnnotation(target, anno, name) != null;
        } catch (NoSuchFieldException | SecurityException e) {
            return false;
        }
    }

    public static <T extends Annotation> T getFieldAnnotation(Class<?> target, Class<T> anno, String name) {
        try {
            return getAnnotation(target, anno, name);
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
    }

    private static Field getField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(name);
    }

    private static <T extends Annotation> T getAnnotation(Class<T> anno, Field field) {
        return field.getDeclaredAnnotation(anno);
    }

    private static <T extends Annotation> T getAnnotation(Class<?> target, Class<T> anno, String name)
            throws NoSuchFieldException, SecurityException {
        Field field = getField(target, name);

        return getAnnotation(anno, field);
    }
}
