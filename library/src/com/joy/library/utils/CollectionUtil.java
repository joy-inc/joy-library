package com.joy.library.utils;

import java.util.Collection;

/**
 * 集合工具类
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {

        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {

        return !isEmpty(collection);
    }

    public static int size(Collection<?> collection) {

        return collection == null ? 0 : collection.size();
    }
}
