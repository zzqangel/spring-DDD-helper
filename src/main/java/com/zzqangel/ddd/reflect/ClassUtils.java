package com.zzqangel.ddd.reflect;

import java.io.Serializable;

public class ClassUtils {

    public static boolean isSuperClass(Class subClass, Class superClass) {
        return superClass.isAssignableFrom(subClass);
    }

}
