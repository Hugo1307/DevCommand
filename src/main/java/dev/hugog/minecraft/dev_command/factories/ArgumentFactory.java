package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.validators.ICommandArgument;

import java.lang.reflect.Constructor;

public class ArgumentFactory implements IObjectFactory<ICommandArgument<?>, Class<? extends ICommandArgument<?>>> {

    private final String argument;

    public ArgumentFactory(String argument) {
        this.argument = argument;
    }

    @Override
    public ICommandArgument<?> generate(Class<? extends ICommandArgument<?>> commandArgumentClass) {

        try {
            Constructor<? extends ICommandArgument<?>> argumentConstructor = commandArgumentClass.getDeclaredConstructor(String.class);
            return argumentConstructor.newInstance(argument);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

    }

}
