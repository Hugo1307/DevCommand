package dev.hugog.minecraft.dev_command.validators;

public interface ICommandArgument<T> {

    boolean isValid();
    T parse();

}
