package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.*;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EntityHandler {
    private static final Set<Object> persistedEntities = new HashSet<>();

    public static void insert(Object entity) throws IllegalAccessException {
        if (persistedEntities.contains(entity)) {
            return; // Evita persistir novamente
        }
        persistedEntities.add(entity);

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

            if (!field.isAnnotationPresent(OneToMany.class) || field.getAnnotation(OneToOne.class).mappedBy().isBlank()) {
                if (!field.isAnnotationPresent(Id.class)) {
                    String columnName;

                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
                        columnName = joinColumnAnnotation.name();
                    } else {
                        columnName = field.getName();
                    }

                    sql.append(columnName).append(", ");

                    Object value = field.get(entity);

                    // Englobando o que for String e Data em aspas
                    if (value instanceof String || value instanceof java.time.LocalDate) {
                        sqlValue.append("'").append(value).append("', ");
                    } else if (value == null || value instanceof ArrayList) {
                        sqlValue.append("null").append(", ");
                    } else if (isEntity(field)) {
                        insert(value); // Persiste a entidade relacionada
                    } else {
                        sqlValue.append(value).append(", ");
                    }
                }
            }
        }

        finalizeSql(sql, sqlValue);
        executeInsert(sql.toString(), entity);
    }

    public static void deleteById(Object entity) throws IllegalAccessException {
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

        // Obtém o valor do ID
        Object idValue = getId(entity);
        if (idValue == null) {
            throw new RuntimeException("Entidade sem ID definida não pode ser excluída.");
        }

        // Processa relacionamentos @OneToMany
        Field[] fields = clzz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(OneToMany.class)) {
                Object relatedEntities = field.get(entity);
                if (relatedEntities instanceof Iterable) {
                    for (Object relatedEntity : (Iterable<?>) relatedEntities) {
                        deleteById(relatedEntity); // Exclui entidades relacionadas
                    }
                }
            }
        }

        // Monta o SQL para exclusão
        String sql = "DELETE FROM " + tableName + " WHERE id = " + idValue;

        // Executa o DELETE
        executeDelete(sql);
    }

    private static void executeDelete(String sql) {
        try (Statement statement = Connection.getManager().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("SQL Executado: " + sql);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir entidade: " + sql, e);
        }
    }


    private static boolean isEntity(Field field) {
        return field.getType().isAnnotationPresent(Entity.class);
    }

    private static Object getId(Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return idField.get(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar ID", e);
        }
    }

    private static Object getFieldValue(Field field, Object entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro ao acessar campo", e);
        }
    }

    private static String formatValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String || value instanceof LocalDate) return "'" + value + "'";
        return value.toString();
    }

    private static void finalizeSql(StringBuilder sql, StringBuilder sqlValue) {
        sql.setLength(sql.length() - 2);
        sqlValue.setLength(sqlValue.length() - 2);
        sql.append(") ").append(sqlValue).append(")");
        System.out.println(sql);
    }

    private static void executeInsert(String sql, Object entity) {
        try (Statement statement = Connection.getManager().createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setGeneratedId(entity, generatedKeys.getLong(1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao persistir entidade", e);
        }
    }

    private static void setGeneratedId(Object entity, long id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID", e);
        }
    }
}