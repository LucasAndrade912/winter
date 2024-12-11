package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DDLHandler {
    public static void createTable(Class<?> clzz) {
        String tableName = clzz.getSimpleName();

        if (clzz.isAnnotationPresent(Entity.class)) {
            Entity entity = clzz.getAnnotation(Entity.class);
            tableName = entity.name();
        }

        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");

        StringBuilder foreignKeys = new StringBuilder();
        Field[] fields = clzz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();
                String columnType;
                StringBuilder constraints = new StringBuilder();

                if (column.primaryKey()) {
                    columnType = "SERIAL PRIMARY KEY";
                } else if (column.foreignKey()) {
                    columnType = "INTEGER";
                } else {
                    columnType = mapJavaTypeToSQLType(field.getType());
                }

                if (column.unique()) {
                    constraints.append(" UNIQUE");
                }

                if (column.notNull()) {
                    constraints.append(" NOT NULL");
                }

                if (column.foreignKey()) {
                    foreignKeys.append("FOREIGN KEY (")
                            .append(column.name())
                            .append(") REFERENCES ")
                            .append(column.references())
                            .append("(").append(column.referenceId()).append("), ");
                }

                sql.append(columnName)
                        .append(" ")
                        .append(columnType)
                        .append(constraints)
                        .append(", ");
            }
        }

        sql.append(foreignKeys);
        sql.setLength(sql.length() - 2);
        sql.append(");");

        java.sql.Connection manager = Connection.getManager();

        try (Statement statement = manager.createStatement()) {
            statement.execute(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static String mapJavaTypeToSQLType(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return "BIGINT";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else if (type == Integer.class || type == int.class) {
            return "INTEGER";
        } else if (type == Double.class || type == double.class) {
            return "DOUBLE PRECISION";
        } else if (type == LocalDate.class) {
            return "DATE";
        }

        return "";
    }
}
