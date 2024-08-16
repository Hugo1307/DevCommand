package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.arguments.parsers.ICommandArgumentParser;

import java.lang.reflect.Constructor;

public class ArgumentParserFactory implements IObjectFactory<ICommandArgumentParser<?>, Class<? extends ICommandArgumentParser<?>>> {

    private final String argument;

    public ArgumentParserFactory(String argument) {
        this.argument = argument;
    }

    @Override
    public ICommandArgumentParser<?> generate(Class<? extends ICommandArgumentParser<?>> commandArgumentClass) {

        try {
            Constructor<? extends ICommandArgumentParser<?>> argumentConstructor = commandArgumentClass.getDeclaredConstructor(String.class);
            return argumentConstructor.newInstance(argument);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

    }

}
