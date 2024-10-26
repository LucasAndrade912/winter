package br.edu.ifpb.webFramework.persistence.annotations;

import java.lang.reflect.Field;

public class Processor {
    public static void process(Object object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();

        if (objectClass.isAnnotationPresent(Entity.class)) {
            Field[] fields = objectClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                System.out.println(field.getName() + " : " + field.get(object));
            }
        }
    }
}
