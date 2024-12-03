package br.edu.ifpb.webFramework.persistence.annotations;

import br.edu.ifpb.webFramework.persistence.DDLHandler;

import java.lang.reflect.Field;

public class Processor {
    public static void process(Object object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();

        if (objectClass.isAnnotationPresent(Entity.class)) {
            DDLHandler.createTable(objectClass);
            System.out.println("Table created successfully!");
        }
    }
}
