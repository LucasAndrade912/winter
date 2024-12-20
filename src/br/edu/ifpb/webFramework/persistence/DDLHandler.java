package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.*;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;

public class DDLHandler {
    public static void createTable(Class<?> clzz) throws Exception {
        if (clzz.isAnnotationPresent(Entity.class)) {
            Entity entity = clzz.getAnnotation(Entity.class);
            String tableName = entity.name().isEmpty() ? clzz.getSimpleName() : entity.name();

            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");

            StringBuilder foreignKeys = new StringBuilder();
            Field[] fields = clzz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                String columnName = field.isAnnotationPresent(Constraints.class)
                        ? field.getAnnotation(Constraints.class).name()
                        : field.getName();

                if (field.isAnnotationPresent(OneToOne.class)) {
                    if (field.getAnnotation(OneToOne.class).mappedBy().isEmpty() && field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);

                        String referenceTable =
                                field.getType().getAnnotation(Entity.class).name().isEmpty()
                                    ? field.getType().getSimpleName()
                                    : field.getType().getAnnotation(Entity.class).name();

                        sql.append(joinColumn.name())
                                .append(" ")
                                .append("INTEGER")
                                .append(", ");

                        foreignKeys.append("FOREIGN KEY (")
                                .append(joinColumn.name())
                                .append(") REFERENCES ")
                                .append(referenceTable)
                                .append("(")
                                .append(joinColumn.referencedId().isEmpty() ? field.getName() : joinColumn.referencedId())
                                .append("), ");
                    } else if (!field.getAnnotation(OneToOne.class).mappedBy().isEmpty()) {
                        verifyMappedByValueExists(field);
                    } else if (field.getAnnotation(OneToOne.class).mappedBy().isEmpty() && !field.isAnnotationPresent(JoinColumn.class)) {
                        throw new Exception("Mapeamento incorreto das classes");
                    }
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                    if (!Collection.class.isAssignableFrom(field.getType())) {
                        throw new Exception("Mapeamento incorreto das classes");
                    }
                } else if (field.isAnnotationPresent(ManyToOne.class)) {
                    Field[] nesttedTypeFields = field.getType().getDeclaredFields();

                    Boolean hasError = true;

                    for (Field nesttedTypeField : nesttedTypeFields) {
                        nesttedTypeField.setAccessible(true);

                        if (nesttedTypeField.isAnnotationPresent(OneToMany.class)) {
                            if (nesttedTypeField.getAnnotation(OneToMany.class).mappedBy().equals(field.getName())) {
                                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);

                                String referenceTable =
                                        field.getType().getAnnotation(Entity.class).name().isEmpty()
                                                ? field.getType().getSimpleName()
                                                : field.getType().getAnnotation(Entity.class).name();

                                sql.append(joinColumn.name())
                                        .append(" ")
                                        .append("INTEGER")
                                        .append(", ");

                                foreignKeys.append("FOREIGN KEY (")
                                        .append(joinColumn.name())
                                        .append(") REFERENCES ")
                                        .append(referenceTable)
                                        .append("(")
                                        .append(joinColumn.referencedId().isEmpty() ? field.getName() : joinColumn.referencedId())
                                        .append("), ");

                                hasError = false;
                            }
                        }
                    }


                    if (hasError) {
                        throw new Exception("Mapeamento incorreto das classes");
                    }
                } else {
                    String columnType = field.isAnnotationPresent(Id.class)
                            ? "SERIAL PRIMARY KEY"
                            : mapJavaTypeToSQLType(field.getType());

                    StringBuilder constraints = new StringBuilder();

                    if (field.isAnnotationPresent(Constraints.class)) {
                        if (field.getAnnotation(Constraints.class).unique()) {
                            constraints.append(" UNIQUE");
                        }

                        if (field.getAnnotation(Constraints.class).nullable()) {
                            constraints.append(" NOT NULL");
                        }
                    }

                    sql.append(columnName)
                            .append(" ")
                            .append(columnType)
                            .append(constraints)
                            .append(", ");
                }
            }

            sql.append(foreignKeys);
            sql.setLength(sql.length() - 2);
            sql.append(");");

            System.out.println(sql);

            java.sql.Connection manager = Connection.getManager();

            try (Statement statement = manager.createStatement()) {
                statement.execute(sql.toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void verifyMappedByValueExists(Field field) throws Exception {
        Class<?> type = field.getType();
        String mappedBy = field.getAnnotation(OneToOne.class).mappedBy();

        try {
            type.getDeclaredField(mappedBy);
        } catch (NoSuchFieldException e) {
            throw new Exception(
                    "Atributo " + mappedBy + " não presente na classe " + field.getType()
            );
        }
    }

    static String mapJavaTypeToSQLType(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return "BIGINT";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else if (type == Integer.class || type == int.class) {
            return "INTEGER";
        } else if (type == Double.class || type == double.class) {
            return "DOUBLE PRECISION";
        } else if (type == LocalDate.class) {
            return "DATE";
        }

        return "";
    }
}
