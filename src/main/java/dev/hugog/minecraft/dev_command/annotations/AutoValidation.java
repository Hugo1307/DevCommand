package dev.hugog.minecraft.dev_command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoValidation {
    boolean permission() default true;
    boolean arguments() default true;
    boolean sender() default true;
}
