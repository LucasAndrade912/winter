package br.edu.ifpb.winter.persistence;

import br.edu.ifpb.winter.persistence.annotations.*;

import java.lang.reflect.Field;

public class ColumnFactory {
    public Column createColumn(Field field) {
        if (field.isAnnotationPresent(OneToOne.class)) {
            return new OneToOneColumn(field);
        } else if (field.isAnnotationPresent(OneToMany.class)) {
            return new OneToManyColumn(field);
        } else if (field.isAnnotationPresent(ManyToOne.class)) {
            return new ManyToOneColumn(field);
        } else {
            return new SimpleColumn(field);
        }
    }
}
