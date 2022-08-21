package me.hgsoft.minecraft.devcommand.factories.validators;

public interface ICommandArgument<T> {

    boolean isValid();
    T parse();

}
