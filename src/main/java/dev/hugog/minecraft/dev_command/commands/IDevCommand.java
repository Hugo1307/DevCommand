package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.parsers.ICommandArgumentParser;
import dev.hugog.minecraft.dev_command.validation.IAutoValidationConfiguration;

import java.util.List;
import java.util.Optional;

public interface IDevCommand {

    void execute();

    List<String> onTabComplete(String[] args);

    /**
     * Get Argument Parser for a mandatory argument at the given position.
     *
     * @param argumentPosition the position of the argument
     * @return the argument parser
     * @throws IllegalArgumentException if the argument is not present
     */
    ICommandArgumentParser<?> getArgumentParser(int argumentPosition);

    /**
     * Get Argument Parser for an optional argument at the given position.
     *
     * @param argumentPosition the position of the argument
     * @return an Optional containing the argument parser if the argument exists, or an empty Optional if not exists
     */
    Optional<ICommandArgumentParser<?>> getOptionalArgumentParser(int argumentPosition);

    boolean hasPermissionToExecuteCommand();

    boolean hasValidArgs();

    boolean canSenderExecuteCommand();

    boolean performAutoValidation(IAutoValidationConfiguration configuration);

    <T> T getDependency(Class<T> dependencyClass);

}
