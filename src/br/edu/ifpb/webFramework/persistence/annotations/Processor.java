package br.edu.ifpb.webFramework.persistence.annotations;

import br.edu.ifpb.webFramework.persistence.DDLHandler;

public class Processor {
    public static void process(Class<?> clz) throws IllegalAccessException {
        if (clz.isAnnotationPresent(Entity.class)) {
            DDLHandler.createTable(clz);
        }
    }
}
