package br.edu.ifpb.webFramework.persistence;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLHandler {
    public static void createTable(Class<?> cls) {
        String tableName = cls.getSimpleName().toLowerCase();

        tableName = tableName.equals("user") ? "users" : tableName;

        StringBuilder sql = new StringBuilder("create table if not exists " + tableName + " (");

        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = field.getName();
            String columnType = mapJavaTypeToSQLType(field.getType());
            sql.append(columnName).append(" ").append(columnType).append(", ");
        }

        sql.setLength(sql.length() - 2);
        sql.append(");");

        java.sql.Connection manager = Connection.getManager();
        System.out.println(sql.toString());

        try (Statement statement = manager.createStatement()) {
            statement.execute(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String mapJavaTypeToSQLType(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return "bigint";
        } else if (type == String.class) {
            return "varchar(255)";
        } else if (type == Integer.class || type == int.class) {
            return "integer";
        } else if (type == Double.class || type == double.class) {
            return "double precision";
        } else if (type == java.time.LocalDate.class) {
            return "date";
        }
        throw new IllegalArgumentException("Type not supported: " + type.getSimpleName());
    }
}
