package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
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

    /**
     * Check if the player has permission to execute the command.
     *
     * @return true if the player has permission, false otherwise
     */
    boolean hasPermissionToExecuteCommand();

    /**
     * Check if all arguments provided are valid.
     *
     * @return true if all arguments are valid, false otherwise
     */
    boolean hasValidArgs();

    /**
     * Check if the command sender can execute the command.
     *
     * @return true if the command sender can execute the command, false otherwise
     */
    boolean canSenderExecuteCommand();

    /**
     * Perform auto-validation of the command.
     *
     * @param configuration the configuration for auto-validation
     * @return true if the command is valid, false otherwise
     */
    boolean performAutoValidation(IAutoValidationConfiguration configuration);

    /**
     * Get a dependency injected into the command.
     *
     * @param dependencyClass the class of the dependency to get
     * @param <T> the type of the dependency
     * @return the dependency instance
     */
    <T> T getDependency(Class<T> dependencyClass);

    /**
     * Get all invalid arguments for the command.
     *
     * @return an array of invalid arguments, or an empty array if all arguments are valid
     */
    CommandArgument[] getInvalidArguments();

}
