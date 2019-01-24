package cn.jiiiiiin.validation.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Style {
    StyleType value() default StyleType.ENTITY;
    Class vo();
}
