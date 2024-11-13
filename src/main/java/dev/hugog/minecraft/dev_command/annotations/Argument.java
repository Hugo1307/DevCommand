package dev.hugog.minecraft.dev_command.annotations;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;

import java.lang.annotation.*;

/**
 * Annotation to define a command argument.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    String name() default "";
    String description() default "";
    int position();
    Class<? extends CommandArgumentParser<?>> parser();
    boolean optional() default false;
}
