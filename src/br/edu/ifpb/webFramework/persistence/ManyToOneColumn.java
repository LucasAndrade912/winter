package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.*;

import java.lang.reflect.Field;

public class ManyToOneColumn extends Column {
    public ManyToOneColumn(Field field) {
        super(field);
    }

    @Override
    public String getDefinition() {
        // Lado fraco, deve ter chave estrangeira
        return columnName + " INTEGER";
    }

    @Override
    public String getForeignKeyDefinition() {
        if (isForeignKey()) {
            String referenceTable = field.getType().getAnnotation(Entity.class).name();
            return "FOREIGN KEY (" + columnName + ") REFERENCES " + referenceTable + "(id)";
        }
        return "";
    }

    @Override
    public String toString() {
        return "ManyToOneColumn{" +
                "field=" + field +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
