package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.arguments.validators.ICommandArgumentValidator;

import java.lang.reflect.Constructor;

public class ArgumentValidatorFactory implements IObjectFactory<ICommandArgumentValidator<?>, Class<? extends ICommandArgumentValidator<?>>> {

    private final String argument;

    public ArgumentValidatorFactory(String argument) {
        this.argument = argument;
    }

    @Override
    public ICommandArgumentValidator<?> generate(Class<? extends ICommandArgumentValidator<?>> commandArgumentClass) {

        try {
            Constructor<? extends ICommandArgumentValidator<?>> argumentConstructor = commandArgumentClass.getDeclaredConstructor(String.class);
            return argumentConstructor.newInstance(argument);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

    }

}
