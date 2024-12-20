package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.ManyToOne;
import br.edu.ifpb.webFramework.persistence.annotations.OneToOne;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {
    public static <T> List<T> executeQuery(String sql, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        try (java.sql.Connection conn = Connection.getManager();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapResultSetToEntity(rs, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private static <T> T mapResultSetToEntity(ResultSet resultSet, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            String columnName = getColumnName(field);

            try {
                if (resultSet.findColumn(columnName) > 0) {
                    Object value = resultSet.getObject(columnName);

                    if (value != null) {
                        // Converte o valor para o tipo correto
                        if (field.getType() == Long.class || field.getType() == long.class) {
                            field.set(entity, ((Number) value).longValue());
                        } else if (field.getType() == Integer.class || field.getType() == int.class) {
                            field.set(entity, ((Number) value).intValue());
                        } else {
                            field.set(entity, value);
                        }
                    }
                }
            } catch (SQLException ignored) {
                // A coluna pode não existir para este campo em uma consulta com JOIN
            }

            // Verifica se é um relacionamento
            if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class)) {
                Class<?> relatedEntityClass = field.getType();
                Object relatedEntity = mapRelatedEntity(resultSet, relatedEntityClass);

                if (relatedEntity != null) {
                    field.set(entity, relatedEntity);

                    // Verifica se a relação é bidirecional e define o pai no filho
                    setParentInChild(field, entity, relatedEntity);
                }
            }
        }

        return entity;
    }

    private static void setParentInChild(Field parentField, Object parentEntity, Object childEntity) throws Exception {
        Class<?> childClass = childEntity.getClass();
        Field[] childFields = childClass.getDeclaredFields();

        for (Field childField : childFields) {
            childField.setAccessible(true);

            // Verifica se o campo do filho é do mesmo tipo que o pai
            if (childField.getType().equals(parentEntity.getClass())) {
                childField.set(childEntity, parentEntity);
                break;
            }
        }
    }


    private static <T> T mapRelatedEntity(ResultSet resultSet, Class<T> relatedEntityClass) throws Exception {
        T relatedEntity = relatedEntityClass.getDeclaredConstructor().newInstance();
        Field[] relatedFields = relatedEntityClass.getDeclaredFields();

        for (Field relatedField : relatedFields) {
            relatedField.setAccessible(true);

            String columnName = getColumnName(relatedField);

            try {
                if (resultSet.findColumn(columnName) > 0) {
                    Object value = resultSet.getObject(columnName);

                    if (value != null) {
                        if (relatedField.getType() == Long.class || relatedField.getType() == long.class) {
                            relatedField.set(relatedEntity, ((Number) value).longValue());
                        } else if (relatedField.getType() == Integer.class || relatedField.getType() == int.class) {
                            relatedField.set(relatedEntity, ((Number) value).intValue());
                        } else {
                            relatedField.set(relatedEntity, value);
                        }
                    }
                }
            } catch (SQLException ignored) {
                // A coluna pode não existir para este campo no contexto do JOIN
            }
        }

        return relatedEntity;
    }


    private static String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            if (!column.name().isEmpty()) {
                return column.name(); // Nome da coluna definido na anotação @Column
            }
        }
        return field.getName(); // Nome do campo será usado como nome da coluna
    }


    private static Object convertValueToFieldType(Object value, Class<?> fieldType) {
        if (fieldType.isAssignableFrom(value.getClass())) {
            return value; // Já é do tipo correto
        }

        if (fieldType == Long.class || fieldType == long.class) {
            return ((Number) value).longValue();
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return ((Number) value).intValue();
        } else if (fieldType == Double.class || fieldType == double.class) {
            return ((Number) value).doubleValue();
        } else if (fieldType == Float.class || fieldType == float.class) {
            return ((Number) value).floatValue();
        } else if (fieldType == String.class) {
            return value.toString();
        }

        throw new IllegalArgumentException("Unsupported field type: " + fieldType.getName());
    }


    private static Map<String, Object> extractColumnData(ResultSet rs) throws Exception {
        Map<String, Object> columnData = new HashMap<>();
        int columnCount = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = rs.getMetaData().getColumnName(i);
            columnData.put(columnName, rs.getObject(i));
        }
        return columnData;
    }
}
