package com.share1024.redisson.util;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * \* @Author: yesheng
 * \* Date: 2021/7/23 17:11
 * \* Description:
 * \
 */
public class PropertyUtil {
    private static int springBootVersion = 1;

    static {
        try {
            Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
        } catch (ClassNotFoundException ignored) {
            springBootVersion = 2;
        }
    }

    /**
     * Whether environment contain properties with specified prefix.
     *
     * @param environment the environment context
     * @param prefix the prefix part of property key
     * @return true if contain, otherwise false
     */
    @SuppressWarnings("unchecked")
    public static boolean containPropertyPrefix(final Environment environment, final String prefix) {
        try {
            Map<String, Object> properties = (Map<String, Object>) (1 == springBootVersion ? v1(environment, prefix, false) : v2(environment, prefix, Map.class));
            return !properties.isEmpty();
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            return false;
        }
    }

    /**
     * Spring Boot 1.x is compatible with Spring Boot 2.x by Using Java Reflect.
     * @param environment : the environment context
     * @param prefix : the prefix part of property key
     * @param targetClass : the target class type of result
     * @param <T> : refer to @param targetClass
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T handle(final Environment environment, final String prefix, final Class<T> targetClass) throws Exception {
        switch (springBootVersion) {
            case 1:
                return (T) v1(environment, prefix, true);
            default:
                return (T) v2(environment, prefix, targetClass);
        }
    }

    @SuppressWarnings("unchecked")
    private static Object v1(final Environment environment, final String prefix, final boolean handlePlaceholder) throws Exception {
        Class<?> resolverClass = Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
        Constructor<?> resolverConstructor = resolverClass.getDeclaredConstructor(PropertyResolver.class);
        Method getSubPropertiesMethod = resolverClass.getDeclaredMethod("getSubProperties", String.class);
        Object resolverObject = resolverConstructor.newInstance(environment);
        String prefixParam = prefix.endsWith(".") ? prefix : prefix + ".";
        Method getPropertyMethod = resolverClass.getDeclaredMethod("getProperty", String.class);
        Map<String, Object> dataSourceProps = (Map<String, Object>) getSubPropertiesMethod.invoke(resolverObject, prefixParam);
        Map<String, Object> propertiesWithPlaceholderResolved = new HashMap<>();
        for (Map.Entry<String, Object> entry : dataSourceProps.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (handlePlaceholder && value instanceof String && ((String) value).contains(PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX)) {
                String resolvedValue = (String) getPropertyMethod.invoke(resolverObject, prefixParam + key);
                propertiesWithPlaceholderResolved.put(key, resolvedValue);
            } else {
                propertiesWithPlaceholderResolved.put(key, value);
            }
        }
        return Collections.unmodifiableMap(propertiesWithPlaceholderResolved);
    }

    private static Object v2(final Environment environment, final String prefix, final Class<?> targetClass) throws Exception {
        Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
        Method getMethod = binderClass.getDeclaredMethod("get", Environment.class);
        Method bindMethod = binderClass.getDeclaredMethod("bind", String.class, Class.class);
        Object binderObject = getMethod.invoke(null, environment);
        String prefixParam = prefix.endsWith(".") ? prefix.substring(0, prefix.length() - 1) : prefix;
        Object bindResultObject = bindMethod.invoke(binderObject, prefixParam, targetClass);
        Method resultGetMethod = bindResultObject.getClass().getDeclaredMethod("get");
        return resultGetMethod.invoke(bindResultObject);
    }
}