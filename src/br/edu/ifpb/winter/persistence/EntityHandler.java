package br.edu.ifpb.winter.persistence;

import br.edu.ifpb.winter.persistence.annotations.*;
import br.edu.ifpb.winter.utils.ClassDataExtracted;
import br.edu.ifpb.winter.utils.ObjectDataExtract;
import br.edu.ifpb.winter.utils.ObjectDataExtracted;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityHandler {
    private static final Set<Object> persistedEntities = new HashSet<>();

    public static void insert(Object entity) throws IllegalAccessException {
        if (persistedEntities.contains(entity)) {
            return; // Evita persistir duplicatas
        }
        persistedEntities.add(entity);

        ObjectDataExtracted extracted = ObjectDataExtract.extract(entity);

        Class<?> clzz = extracted.getClass();
        ClassDataExtracted dataExtracted = extracted.getClassDataExtracted();
        if (!dataExtracted.getEntity()) {
            throw new RuntimeException("Classe não é uma entidade gerenciada.");
        }

        String tableName = dataExtracted.getTableName();
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder sqlValues = new StringBuilder("VALUES (");

        List<Column> columns = dataExtracted.getColumns();
        List<Object> oneToManyCollections = new ArrayList<>();

        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                continue; // Ignorar campo ID na inserção (assume-se que ele será gerado)
            }

            Field field = column.getField();
            field.setAccessible(true);
            Object value = field.get(entity);
            if (value == null) {
                continue; // Ignorar campos nulos
            }

            if (column.isJoinColumn() && column.isEntity()) {
                // Relacionamento unidirecional
                Object relatedId = getId(value);
                if (relatedId == null) {
                    insert(value); // Persistir a entidade relacionada
                    relatedId = getId(value);
                }

                appendColumnAndValue(sql, sqlValues, column.getColumnName(), relatedId);
            } else if (field.isAnnotationPresent(OneToMany.class) && isCollection(field)) {
                // Relacionamento OneToMany (guardar para persistir após o pai)
                oneToManyCollections.add(field);
            } else if (column.isEntity()) {
                // Relacionamento bidirecional ou embutido
                insert(value); // Persistir a entidade relacionada
                Object relatedId = getId(value);
                appendColumnAndValue(sql, sqlValues, field.getName(), relatedId);
            } else {
                appendColumnAndValue(sql, sqlValues, resolveColumnName(field), value);
            }
        }

        finalizeSql(sql, sqlValues);
        executeInsert(sql.toString(), entity);

        // Persistir coleções OneToMany após o pai
        for (Object collectionField : oneToManyCollections) {
            persistCollection((Field) collectionField, entity);
        }
    }

    private static void persistCollection(Field field, Object parentEntity) throws IllegalAccessException {
        List<?> collection = (List<?>) field.get(parentEntity);
        if (collection == null || collection.isEmpty()) {
            return; // Ignorar coleções vazias
        }

        Object parentId = getId(parentEntity);
        if (parentId == null) {
            throw new RuntimeException("Entidade pai deve ser persistida antes de seus filhos.");
        }

        for (Object childEntity : collection) {
            setParentReference(childEntity, parentEntity, field);
            insert(childEntity); // Persistir cada item da coleção
        }
    }

    private static void setParentReference(Object childEntity, Object parentEntity, Field collectionField) {
        Field[] childFields = childEntity.getClass().getDeclaredFields();
        for (Field childField : childFields) {
            childField.setAccessible(true);
            if (childField.isAnnotationPresent(ManyToOne.class)) {
                try {
                    childField.set(childEntity, parentEntity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Erro ao configurar referência ManyToOne.", e);
                }
                break;
            }
        }
    }

    private static String resolveColumnName(Field field) {
        if (field.isAnnotationPresent(Constraints.class)) {
            Constraints columnAnnotation = field.getAnnotation(Constraints.class);
            if (!columnAnnotation.name().isEmpty()) {
                return columnAnnotation.name();
            }
        }
        return field.getName();
    }

    private static boolean isCollection(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    private static Object getId(Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return idField.get(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar ID da entidade.", e);
        }
    }

    private static void appendColumnAndValue(StringBuilder sql, StringBuilder sqlValues, String columnName, Object value) {
        sql.append(columnName).append(", ");
        sqlValues.append(formatValue(value)).append(", ");
    }

    private static String formatValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String || value instanceof LocalDate) return "'" + value + "'";
        return value.toString();
    }

    private static void finalizeSql(StringBuilder sql, StringBuilder sqlValues) {
        sql.setLength(sql.length() - 2); // Remove a última vírgula
        sqlValues.setLength(sqlValues.length() - 2); // Remove a última vírgula
        sql.append(") ").append(sqlValues).append(")");
    }

    private static void executeInsert(String sql, Object entity) {
        System.out.println(sql);

        try (Statement statement = Connection.getManager().createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setGeneratedId(entity, generatedKeys.getLong(1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao persistir entidade.", e);
        }
    }

    private static void setGeneratedId(Object entity, long id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID gerado.", e);
        }
    }

    // --------

    public static void deleteById(Object entity, String id) throws IllegalAccessException {
        if (entity == null) {
            return; // Não faz nada se a entidade for nula
        }

        Class<?> clzz = entity.getClass();

        // Verifica se é uma entidade válida
        if (!clzz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("A classe não é uma entidade gerenciada.");
        }

        // Obtém o nome da tabela
        String tableName = clzz.getAnnotation(Entity.class).name();
        if (tableName.isEmpty()) {
            tableName = clzz.getSimpleName().toLowerCase();
        }

        // Monta o SQL para exclusão
        String sql = "DELETE FROM " + tableName + " WHERE id = " + id;

        // Executa o DELETE
        executeDelete(sql);
    }

    public static void update(Object entity) throws IllegalAccessException {
        if (entity == null) {
            return;
        }

        ObjectDataExtracted extracted = ObjectDataExtract.extract(entity);
        ClassDataExtracted dataExtracted = extracted.getClassDataExtracted();
        Class<?> clzz = dataExtracted.getClass();

        String tableName = dataExtracted.getTableName();

        if (!clzz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("A classe não é uma entidade gerenciada.");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");

        List<Column> columns = dataExtracted.getColumns();
        for (Column column : columns) {
            Field field = column.getField();

            if (!field.isAnnotationPresent(OneToMany.class)) {
                if (!column.isEntity()) {
                    String columnName = column.getColumnName();

                    sql.append(columnName).append(" = ");

                    Object value = getFieldValue(field, entity);

                    // Englobando o que for String e Data em aspas
                    if (value instanceof String || value instanceof java.time.LocalDate) {
                        sql.append("'").append(value).append("', ");
                    } else if (value == null || value instanceof ArrayList) {
                        sql.append("null").append(", ");
                    } else if (column.isEntity()) {
                        update(value); // Persiste a entidade relacionada
                    } else {
                        sql.append(value).append(", ");
                    }
                }
            }
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ").append(getId(entity));
        executeUpdate(sql.toString(), entity);
    }

    private static void executeDelete(String sql) {
        try (Statement statement = Connection.getManager().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("SQL Executado: " + sql);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir entidade: " + sql, e);
        }
    }

    private static Object getFieldValue(Field field, Object entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro ao acessar campo", e);
        }
    }

    private static void executeUpdate(String sql, Object entity) {
        try (Statement statement = Connection.getManager().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("SQL Executado: " + sql);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar entidade: " + sql, e);
        }
    }
}