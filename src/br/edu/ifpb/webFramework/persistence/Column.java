package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.*;

import java.lang.reflect.Field;

public abstract class Column {
    protected Field field;
    protected String columnName;
    protected boolean isPrimaryKey = false;
    protected boolean isEntity = false;
    protected boolean isJoinColumn = false;
    protected boolean isForeignKey = false;
    protected boolean isOneToManyColumn = false;

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
            this.isForeignKey = true;
            this.isJoinColumn = true;
            this.columnName = field.getAnnotation(JoinColumn.class).name().isEmpty()
                    ? field.getName() + "_id"
                    : field.getAnnotation(JoinColumn.class).name();
        }

        if (field.isAnnotationPresent(OneToOne.class)) {
            if (!field.getAnnotation(OneToOne.class).mappedBy().isEmpty()) {
                this.columnName = field.getName() + "_id";
                this.isForeignKey = true;
            }
        }

        if (field.isAnnotationPresent(OneToMany.class)) {
            this.isOneToManyColumn = true;
        }

        if (field.isAnnotationPresent(ManyToOne.class) && field.isAnnotationPresent(JoinColumn.class)) {
            this.isForeignKey = true;
        }
    }

    public abstract String getDefinition();

    public boolean isOneToManyColumn() {
        return isOneToManyColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isForeignKey() {
        return isForeignKey;
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
