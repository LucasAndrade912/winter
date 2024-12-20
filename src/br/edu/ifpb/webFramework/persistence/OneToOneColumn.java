package br.edu.ifpb.webFramework.persistence;

import br.edu.ifpb.webFramework.persistence.annotations.*;

import java.lang.reflect.Field;

public class OneToOneColumn extends Column {

    public OneToOneColumn(Field field) {
        super(field);
    }

    @Override
    public String getDefinition() {
        if (field.isAnnotationPresent(OneToOne.class)) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (!oneToOne.mappedBy().isEmpty()) {
                // Lado forte, não deve ter chave estrangeira
                return columnName + "_id INTEGER";
            }
        }
        // Lado fraco, deve ter chave estrangeira
        return columnName + "_id INTEGER";
    }

    @Override
    public boolean isForeignKey() {
        if (field.isAnnotationPresent(OneToOne.class)) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            return oneToOne.mappedBy().isEmpty();
        }
        return false;
    }

    @Override
    public String getForeignKeyDefinition() {
        if (isForeignKey()) {
            String referenceTable = field.getType().getAnnotation(Entity.class).name();
            return "FOREIGN KEY (" + columnName + "_id) REFERENCES " + referenceTable + "(id)";
        }
        return "";
    }
}
