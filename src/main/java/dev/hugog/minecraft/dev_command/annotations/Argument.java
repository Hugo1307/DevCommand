package dev.hugog.minecraft.dev_command.annotations;

import dev.hugog.minecraft.dev_command.arguments.validators.CommandArgumentValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to define a command argument.
 */
@Target(ElementType.TYPE)
public @interface Argument {
    String name() default "";
    String description() default "";
    int position();
    Class<? extends CommandArgumentValidator<?>> validator();
    boolean optional() default false;
}
