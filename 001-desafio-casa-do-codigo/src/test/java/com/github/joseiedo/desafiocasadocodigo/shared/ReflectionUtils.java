package com.github.joseiedo.desafiocasadocodigo.shared;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void setFieldValue(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }
}
