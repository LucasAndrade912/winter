package br.edu.ifpb.webFramework.persistence;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLHandler {
    public static void createTable(Class<?> cls) {
        String tableName = cls.getSimpleName();

        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + "(");

        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = field.getName();
            String columnType = mapJavaTypeToSQLType(field.getType());
            sql.append(columnName).append(" ").append(columnType).append(", ");
        }

        sql.setLength(sql.length() - 2);
        sql.append(");");
        System.out.println(sql.toString());

        java.sql.Connection manager = Connection.getManager();

        try (Statement statement = manager.createStatement()) {
            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String mapJavaTypeToSQLType(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return "BIGINT";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else if (type == Integer.class || type == int.class) {
            return "INTEGER";
        } else if (type == java.time.LocalDate.class) {
            return "DATE";
        }
        throw new IllegalArgumentException("Type not supported: " + type.getSimpleName());
    }
}
