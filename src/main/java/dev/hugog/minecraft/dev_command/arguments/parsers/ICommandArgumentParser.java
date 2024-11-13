package dev.hugog.minecraft.dev_command.arguments.parsers;

public interface ICommandArgumentParser<T> {

    boolean isValid();
    T parse();

}
