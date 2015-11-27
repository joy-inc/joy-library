package com.android.library.utils;

import java.util.Collection;
import java.util.Map;

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

    public static boolean isEmpty(Map<?, ?> map) {

        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {

        return !isEmpty(map);
    }

    public static int size(Collection<?> collection) {

        return collection == null ? 0 : collection.size();
    }
}