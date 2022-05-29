package com.rabbit.study.util;

public final class LockKeyUtil {

    private static final String KEY_TEMPLATE = "lock:%s";

    private LockKeyUtil() {}

    public static String getLockKey(String key) {
        return String.format(KEY_TEMPLATE, key);
    }
}
