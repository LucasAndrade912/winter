package br.edu.ifpb.winter.utils;

import br.edu.ifpb.winter.persistence.Column;
import br.edu.ifpb.winter.persistence.ColumnFactory;
import br.edu.ifpb.winter.persistence.annotations.Entity;

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
