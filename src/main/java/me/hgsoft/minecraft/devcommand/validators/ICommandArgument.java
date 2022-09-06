package me.hgsoft.minecraft.devcommand.validators;

public interface ICommandArgument<T> {

    boolean isValid();
    T parse();

}
