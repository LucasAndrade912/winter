package br.edu.ifpb.webFramework.persistence.annotations;

import br.edu.ifpb.webFramework.persistence.DDLHandler;
import br.edu.ifpb.webFramework.persistence.EntityHandler;

public class Processor {
    public static void process(Class<?> clz) throws IllegalAccessException {
        DDLHandler.createTable(clz);
    }

    public static void processInsert(Object entity) throws IllegalAccessException {
        EntityHandler.insert(entity);
    }
}
