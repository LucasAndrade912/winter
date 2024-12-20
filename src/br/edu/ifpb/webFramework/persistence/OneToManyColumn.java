package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.*;

import java.lang.reflect.Field;

public class OneToManyColumn extends Column {

    public OneToManyColumn(Field field) {
        super(field);
    }

    @Override
    public String getDefinition() {
        if (field.isAnnotationPresent(OneToMany.class)) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany.mappedBy().isEmpty()) {
                // Lado forte, não deve ter chave estrangeira
                return columnName + "_id INTEGER";
            }
        }
        // Lado fraco, deve ter chave estrangeira
        return "";
    }

    @Override
    public boolean isForeignKey() {
        if (field.isAnnotationPresent(OneToMany.class)) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            return oneToMany.mappedBy().isEmpty();
        }
        return false;
    }

    @Override
    public String getForeignKeyDefinition() {
        if (isForeignKey()) {
            String referenceTable = field.getType().getAnnotation(Entity.class).name();
            return "FOREIGN KEY (" + columnName + ") REFERENCES " + referenceTable + "(id)";
        }
        return "";
    }
}
