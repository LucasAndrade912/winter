package br.edu.ifpb.webFramework.persistence;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

import static br.edu.ifpb.webFramework.persistence.DDLHandler.mapJavaTypeToSQLType;

public class EntityHandler {
    public static void insert(Object entity) {
        Class<?> clz = entity.getClass();

        // Pegando nome da classe
        String tableName = clz.getSimpleName().toLowerCase();

        // Corrigindo erro que da em usar User no bd transformando user em users
        tableName = tableName.equals("user") ? "users" : tableName;

        // Começa o código sql separando no nome da tabela e seus campos e seus valores
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(");
        StringBuilder sqlvalue = new StringBuilder("VALUES (");

        // Pegando os atributos da classe e tratando eles
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            String columnName = field.getName();
            sql.append(columnName).append(", ");

            try {
                Object value = field.get(entity);

                // Englobando o que for String e Data em aspas
                if (value instanceof String || value instanceof java.time.LocalDate) {
                    sqlvalue.append("'").append(value).append("', ");
                } else if (value == null) {
                    sqlvalue.append("null").append(", ");
                } else {
                    sqlvalue.append(value).append(", ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        // Tirando os dois últimos caracteres que são ", "
        sql.setLength(sql.length() - 2);
        sqlvalue.setLength(sqlvalue.length() - 2);

        // Finalizando as duas partes do sql
        sql.append(") ");
        sqlvalue.append(")");

        sql.append(sqlvalue.toString());

        // Fazendo conexão com o bd
        java.sql.Connection manager = Connection.getManager();
        System.out.println(sql.toString());

        try (Statement statement = manager.createStatement()) {
            statement.execute(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
