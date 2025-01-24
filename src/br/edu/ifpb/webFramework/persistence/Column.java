package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.Constraints;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;
import br.edu.ifpb.webFramework.persistence.annotations.Id;
import br.edu.ifpb.webFramework.persistence.annotations.JoinColumn;

import java.lang.reflect.Field;

public abstract class Column {
    protected Field field;
    protected String columnName;
    protected boolean isPrimaryKey = false;
    protected boolean isEntity = false;
    protected boolean isJoinColumn = false;

    public Column(Field field) {
        this.field = field;
        this.columnName = field.isAnnotationPresent(Constraints.class)
                ? field.getAnnotation(Constraints.class).name()
                : field.getName();

        if (field.isAnnotationPresent(Id.class)) {
            this.isPrimaryKey = true;
        }

        if (field.getType().isAnnotationPresent(Entity.class)) {
            this.isEntity = true;
        }

        if (field.isAnnotationPresent(JoinColumn.class)) {
            this.isJoinColumn = true;
            this.columnName = field.getAnnotation(JoinColumn.class).name().isEmpty()
                    ? field.getName() + "_id"
                    : field.getAnnotation(JoinColumn.class).name();
        }
    }

    public abstract String getDefinition();

    public String getColumnName() {
        return columnName;
    }

    public boolean isForeignKey() {
        return false;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public boolean isJoinColumn() {
        return isJoinColumn;
    }

    public Field getField() {
        return field;
    }

    public String getForeignKeyDefinition() {
        return "";
    }
}
