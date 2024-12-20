package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.Constraints;

import java.lang.reflect.Field;

public abstract class Column {

    protected Field field;
    protected String columnName;

    public Column(Field field) {
        this.field = field;
        this.columnName = field.isAnnotationPresent(Constraints.class)
                ? field.getAnnotation(Constraints.class).name()
                : field.getName();
    }

    public abstract String getDefinition();

    public boolean isForeignKey() {
        return false;
    }

    public String getForeignKeyDefinition() {
        return "";
    }
}
