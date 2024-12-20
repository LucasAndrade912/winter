package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.Entity;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLHandler {

    private static final ColumnFactory columnFactory = new ColumnFactory();

    public static void createTable(Class<?> clzz) throws Exception {
        if (clzz.isAnnotationPresent(Entity.class)) {
            Entity entity = clzz.getAnnotation(Entity.class);
            String tableName = entity.name().isEmpty() ? clzz.getSimpleName() : entity.name();

            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");
            StringBuilder foreignKeys = new StringBuilder();

            Field[] fields = clzz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                // Criar coluna
                Column column = columnFactory.createColumn(field);
                if (!column.getDefinition().isEmpty()) {
                    sql.append(column.getDefinition()).append(", ");
                }

                // Adicionar chaves estrangeiras se existirem
                if (column.isForeignKey()) {
                    foreignKeys.append(column.getForeignKeyDefinition()).append(", ");
                }
            }

            sql.append(foreignKeys);
            sql.setLength(sql.length() - 2); // Remover última vírgula
            sql.append(");");

            System.out.println(sql);

            // Executar SQL
            try (Statement statement = Connection.getManager().createStatement()) {
                statement.execute(sql.toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
