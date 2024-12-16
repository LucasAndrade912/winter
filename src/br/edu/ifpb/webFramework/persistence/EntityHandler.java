//package br.edu.ifpb.webFramework.persistence;
//
//import br.edu.ifpb.webFramework.persistence.annotations.Column;
//import br.edu.ifpb.webFramework.persistence.annotations.Entity;
//
//import java.lang.reflect.Field;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Set;
//
//public class EntityHandler {
//    private static final Set<Object> persistedEntities = new HashSet<>();
//
//    public static void insert(Object entity) {
//        if (persistedEntities.contains(entity)) {
//            return; // Evita persistir novamente
//        }
//        persistedEntities.add(entity);
//
//        Class<?> clzz = entity.getClass();
//
//        String tableName = clzz.getSimpleName().toLowerCase();
//        if (clzz.isAnnotationPresent(Entity.class)) {
//            Entity entityAnnotation = clzz.getAnnotation(Entity.class);
//            tableName = entityAnnotation.name();
//        }
//
//        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(");
//        StringBuilder sqlValue = new StringBuilder("VALUES (");
//
//        Field[] fields = clzz.getDeclaredFields();
//        for (Field field : fields) {
//            field.setAccessible(true);
//
//            if (field.isAnnotationPresent(Column.class)) {
//                Column column = field.getAnnotation(Column.class);
//                String columnName = column.name();
//
//                sql.append(columnName).append(", ");
//                Object value = getFieldValue(field, entity);
//
//                System.out.println(field);
//                System.out.println(isEntity(field));
//                if (isEntity(field)) {
//                    insert(value); // Persiste a entidade relacionada
//                    value = getId(value); // Obtem ID da entidade persistida
//                }
//
//                sqlValue.append(column.primaryKey() ? "default" : formatValue(value)).append(", ");
//            }
//        }
//
//        finalizeSql(sql, sqlValue);
//        executeInsert(sql.toString(), entity);
//    }
//
//    private static boolean isEntity(Field field) {
//        return field.getType().isAnnotationPresent(Entity.class);
//    }
//
//    private static Object getId(Object entity) {
//        try {
//            Field idField = entity.getClass().getDeclaredField("id");
//            idField.setAccessible(true);
//            return idField.get(entity);
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao acessar ID", e);
//        }
//    }
//
//    private static Object getFieldValue(Field field, Object entity) {
//        try {
//            return field.get(entity);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException("Erro ao acessar campo", e);
//        }
//    }
//
//    private static String formatValue(Object value) {
//        if (value == null) return "null";
//        if (value instanceof String || value instanceof LocalDate) return "'" + value + "'";
//        return value.toString();
//    }
//
//    private static void finalizeSql(StringBuilder sql, StringBuilder sqlValue) {
//        sql.setLength(sql.length() - 2);
//        sqlValue.setLength(sqlValue.length() - 2);
//        sql.append(") ").append(sqlValue).append(")");
//    }
//
//    private static void executeInsert(String sql, Object entity) {
//        try (Statement statement = Connection.getManager().createStatement()) {
//            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//            ResultSet generatedKeys = statement.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                setGeneratedId(entity, generatedKeys.getLong(1));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao persistir entidade", e);
//        }
//    }
//
//    private static void setGeneratedId(Object entity, long id) {
//        try {
//            Field idField = entity.getClass().getDeclaredField("id");
//            idField.setAccessible(true);
//            idField.set(entity, id);
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao definir ID", e);
//        }
//    }
//}
