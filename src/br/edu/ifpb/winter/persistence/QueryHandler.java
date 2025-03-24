package br.edu.ifpb.winter.persistence;

import br.edu.ifpb.winter.utils.ClassDataExtract;
import br.edu.ifpb.winter.utils.ClassDataExtracted;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class QueryHandler {
    public static <T> List<T> createQuery(Class<T> entity) {
        ClassDataExtracted data = ClassDataExtract.extract(entity);

        StringBuilder query = new StringBuilder("SELECT * FROM ");

        query.append(data.getTableName());
        query.append(";");

        System.out.println(query);
        return executeQuery(query.toString(), entity);
    }

    public static <T> List<T> createQuery(Class<T> entity, Map<String, String> fields) {
        ClassDataExtracted data = ClassDataExtract.extract(entity);

        StringBuilder query = new StringBuilder("SELECT * FROM ");

        query.append(data.getTableName());
        query.append(" WHERE ");

        fields.forEach((key, value) -> {
            query.append(key).append("=");

            if (isNumeric(value)) {
                query.append(value).append(" AND ");

            } else {
                query.append("'").append(value).append("' AND ");
            }
        });

        query.setLength(query.length() - 5);
        query.append(";");

        System.out.println(query);
        return executeQuery(query.toString(), entity);
    }

    private static <T> List<T> executeQuery(String query, Class<T> entity) {
        java.sql.Connection manager = Connection.getManager();

        try (PreparedStatement statement = manager.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            List<T> data = mapResultSetToList(resultSet, entity);
            return data;
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> mapResultSetToList(ResultSet resultSet, Class<T> entity) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException, NoSuchFieldException {
        List<T> data = new ArrayList<>();

        while (resultSet.next()) {
            T entityInstance = entity.getDeclaredConstructor().newInstance();
            ClassDataExtracted entityData = ClassDataExtract.extract(entity);

            List<Column> columns = entityData.getColumns();

            for (Column column : columns) {
                column.getField().setAccessible(true);

                if (!column.isOneToManyColumn()) {
                    Field declaredField;

                    if (column.isForeignKey()) {
                        break;
                    } else {
                        declaredField = entity.getDeclaredField(column.getColumnName());
                    }

                    declaredField.setAccessible(true);
                    Class targetCasting = declaredField.getType();
                    Object object = resultSet.getObject(column.getColumnName());

                    if (targetCasting == Long.class || targetCasting == long.class) {
                        declaredField.set(entityInstance, ((Number) object).longValue());
                    } else if (targetCasting == Integer.class || targetCasting == int.class) {
                        declaredField.set(entityInstance, ((Number) object).intValue());
                    } else if (targetCasting == LocalDate.class) {
                        declaredField.set(entityInstance, ((Date) object).toLocalDate());
                    } else {
                        declaredField.set(entityInstance, object);
                    }
                }
            }

            data.add(entityInstance);
        }

        return data;
    }

    private static boolean isNumeric(String str) {
        return str.matches("[0-9.]+");
    }
}
