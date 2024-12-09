package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.OneToOne;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DDLHandler {
    public static void createTable(Class<?> cls) {
        String tableName = cls.getSimpleName().toLowerCase();
        List<String> foreignKeys = new ArrayList<>();

        tableName = tableName.equals("user") ? "users" : tableName;

        StringBuilder sql = new StringBuilder("create table if not exists " + tableName + " (");

        Field[] fields = cls.getDeclaredFields();

        Stream<Field> fieldStream = Arrays.stream(fields).filter(field -> field.getName().equalsIgnoreCase("id"));

        if (fieldStream.findFirst().isEmpty())
            sql.append("id serial primary key, ");

        for (Field field : fields) {
            field.setAccessible(true);

            String columnName = field.getName();
            String columnType = mapJavaTypeToSQLType(field.getType());

            if (field.isAnnotationPresent(OneToOne.class)) {
                OneToOne annotation = field.getAnnotation(OneToOne.class);

                if (annotation.mappedBy().isEmpty()) {
                    System.out.println(field.getName());

                    foreignKeys.add(field.getName().concat("_id"));

                    if (columnType.equalsIgnoreCase("TYPE"))
                        sql.append(columnName).append("_id ").append("integer").append(", ");
                }
            } else {
                sql.append(columnName).append(" ").append(columnType).append(", ");
            }
        }

        foreignKeys.forEach(foreignKey -> {
            sql.append("foreign key (").append(foreignKey).append(") references ").append(foreignKey, 0, foreignKey.length() - 3).append("(id), ");
        });

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
        } else {
            return "TYPE";
        }
    }
}
