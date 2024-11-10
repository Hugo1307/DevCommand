package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.parsers.ICommandArgumentParser;
import dev.hugog.minecraft.dev_command.validation.IAutoValidationConfiguration;

import java.util.List;

public interface IDevCommand {

    void execute();

    List<String> onTabComplete(String[] args);

    ICommandArgumentParser<?> getArgumentParser(int argumentPosition);
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();
    boolean canSenderExecuteCommand();
    boolean performAutoValidation(IAutoValidationConfiguration configuration);
    <T> T getDependency(Class<T> dependencyClass);

}
