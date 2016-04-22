package com.humane.util;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

public class ObjectConvert {

    public static Map<String, String> asMap(Object object) {
        return asMap(object, false);
    }

    public static Map<String, String> asMap(Object object, boolean useEmpty) {
        try {
            Map<String, String> map = BeanUtils.describe(object);

            if (!useEmpty) {
                Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    String value = entry.getValue();
                    if (value == null || value.equals("")) iterator.remove();
                }
            }
            return map;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
