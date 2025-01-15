package br.edu.ifpb.webFramework.utils;

import br.edu.ifpb.webFramework.persistence.Column;
import br.edu.ifpb.webFramework.persistence.ColumnFactory;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDataExtract {
    private static final ColumnFactory columnFactory = new ColumnFactory();

    public static ClassDataExtracted extract(Class<?> clzz) {
        String className = clzz.getSimpleName();
        Boolean isEntity = false;
        Field[] fields = clzz.getDeclaredFields();
        String tableName = "";
        List<Column> columns = Arrays.stream(fields).map(columnFactory::createColumn).collect(Collectors.toList());

        if (clzz.isAnnotationPresent(Entity.class)) {
            Entity entity = clzz.getAnnotation(Entity.class);
            isEntity = true;
            tableName = entity.name().isEmpty() ? className : entity.name();
        }

        return new ClassDataExtracted(className, isEntity, fields, fields.length, tableName, columns, columns.size());
    }
}
