package br.edu.ifpb.webFramework.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";
    boolean primaryKey() default false;
    boolean unique() default false;
    boolean notNull() default false;
    boolean foreignKey() default false;
    String references() default "";
    String referenceId() default "";
}
