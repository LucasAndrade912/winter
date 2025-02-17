package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.utils.ClassDataExtract;
import br.edu.ifpb.webFramework.utils.ClassDataExtracted;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DDLHandler {
    private static final ColumnFactory columnFactory = new ColumnFactory();

    public static void createTable(Class<?> clzz) throws Exception {
        ClassDataExtracted extracted = ClassDataExtract.extract(clzz);

        if (extracted.getEntity()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + extracted.getTableName() + "(");
            StringBuilder foreignKeys = new StringBuilder();

            List<Column> columns = extracted.getColumns();

            for (Column column : columns) {
                column.getField().setAccessible(true);

                if (!column.getDefinition().isEmpty()) {
                    sql.append(column.getDefinition()).append(", ");
                }

                // Adicionar chaves estrangeiras se existirem
                if (column.isForeignKey() && column.isJoinColumn()) {
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
