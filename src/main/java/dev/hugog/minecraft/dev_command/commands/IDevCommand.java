package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.parsers.ICommandArgumentParser;

public interface IDevCommand {

    void execute();
    ICommandArgumentParser<?>[] parseArguments();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();
    boolean canSenderExecuteCommand();
    <T> T getDependency(Class<T> dependencyClass);

}
