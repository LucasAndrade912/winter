package br.edu.ifpb.winter.persistence;

import br.edu.ifpb.winter.persistence.annotations.*;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class SimpleColumn extends Column {

    public SimpleColumn(Field field) {
        super(field);
    }

    @Override
    public String getDefinition() {
        if (field.isAnnotationPresent(Id.class)) {
            return columnName + " SERIAL PRIMARY KEY";
        }

        String columnType = mapJavaTypeToSQLType(field.getType());
        StringBuilder constraints = new StringBuilder();

        if (field.isAnnotationPresent(Constraints.class)) {
            Constraints cons = field.getAnnotation(Constraints.class);
            if (cons.unique()) {
                constraints.append(" UNIQUE");
            }
            if (cons.nullable()) {
                constraints.append(" NOT NULL");
            }
        }

        return columnName + " " + columnType + constraints;
    }

    private String mapJavaTypeToSQLType(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return "INTEGER";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else if (type == Integer.class || type == int.class) {
            return "INTEGER";
        } else if (type == Double.class || type == double.class) {
            return "DOUBLE PRECISION";
        } else if (type == LocalDate.class) {
            return "DATE";
        }
        return "TEXT";
    }

    @Override
    public String toString() {
        return "SimpleColumn{" +
                "field=" + field +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
