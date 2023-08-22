package com.orangedragon1.server.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RequestUtils {

    public static List<String> listOfNull(Object requestObj) {
        /*
        returns the name names that are null
         */
        List<String> emptyFieldList = new ArrayList<>();
        Class<?> requestObjClass = requestObj.getClass();
        Field[] fields = requestObjClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Access private fields if needed
            try {
                if (field.get(requestObj) == null) {
                    emptyFieldList.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return emptyFieldList;
    }
}
