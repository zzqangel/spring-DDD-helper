package com.zzqangel.ddd.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FieldUtils {

    public static List<Field> getDeclaredFieldList(Class c) {
        return Arrays.asList(c.getDeclaredFields());
    }

    public static void setField(Field field, Object o, Object value) {
        if(!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(o, value);
        } catch (IllegalAccessException e) {

        }
    }

    public static String getName(Field field) {
        return field.getName();
    }

    public static Class getType(Field field) {
        return field.getType();
    }

}
