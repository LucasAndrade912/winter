package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class EntityHandler {
    public static void insert(Object entity) {
        Class<?> clzz = entity.getClass();

        String tableName = clzz.getSimpleName().toLowerCase();
        if (clzz.isAnnotationPresent(Entity.class)) {
            Entity entityAnnotation = clzz.getAnnotation(Entity.class);
            tableName = entityAnnotation.name();
        }

        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(");
        StringBuilder sqlValue = new StringBuilder("VALUES (");

        Field[] fields = clzz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                sql.append(columnName).append(", ");

                try {
                    Object value = field.get(entity);

                    if (value instanceof String || value instanceof LocalDate) {
                        sqlValue.append("'").append(value).append("', ");
                    } else if (value == null) {
                        if (column.primaryKey()) {
                            sqlValue.append("default").append(", ");
                        } else {
                            sqlValue.append("null").append(", ");
                        }
                    } else {
                        sqlValue.append(value).append(", ");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Erro ao acessar o campo: " + field.getName(), e);
                }
            }
        }

        sql.setLength(sql.length() - 2);
        sqlValue.setLength(sqlValue.length() - 2);

        sql.append(") ");
        sqlValue.append(")");
        sql.append(sqlValue);

        java.sql.Connection manager = Connection.getManager();
        try (Statement statement = manager.createStatement()) {
            System.out.println(sql);
            statement.execute(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar a inserção: " + sql, e);
        }
    }
}
