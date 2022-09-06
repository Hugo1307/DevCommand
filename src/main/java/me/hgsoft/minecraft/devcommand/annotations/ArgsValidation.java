package me.hgsoft.minecraft.devcommand.annotations;

import me.hgsoft.minecraft.devcommand.validators.CommandArgument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ArgsValidation {
    Class<? extends CommandArgument<?>>[] argsTypes();
    int mandatory() default 0;
}
