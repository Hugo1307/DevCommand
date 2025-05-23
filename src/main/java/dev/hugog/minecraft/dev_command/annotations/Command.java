package dev.hugog.minecraft.dev_command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    String alias();
    String description() default "";
    String permission() default "";
    boolean isPlayerOnly() default false;
}
