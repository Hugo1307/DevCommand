package dev.hugog.minecraft.dev_command.arguments.validators;

public interface ICommandArgumentValidator<T> {

    boolean isValid();
    T parse();

}
