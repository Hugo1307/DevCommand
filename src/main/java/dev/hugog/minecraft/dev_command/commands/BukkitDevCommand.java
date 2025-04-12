package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.exceptions.InvalidArgumentsException;
import dev.hugog.minecraft.dev_command.exceptions.InvalidDependencyException;
import dev.hugog.minecraft.dev_command.validation.IAutoValidationConfiguration;
import lombok.Generated;
import lombok.Getter;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.dependencies.DependencyHandler;
import dev.hugog.minecraft.dev_command.exceptions.ArgumentsConfigException;
import dev.hugog.minecraft.dev_command.exceptions.PermissionConfigException;
import dev.hugog.minecraft.dev_command.factories.ArgumentParserFactory;
import dev.hugog.minecraft.dev_command.arguments.parsers.ICommandArgumentParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Generated
@Getter
public abstract class BukkitDevCommand implements IDevCommand {

    private final BukkitCommandData commandData;
    private final CommandSender commandSender;
    private final String[] args;

    public BukkitDevCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        this.commandData = commandData;
        this.commandSender = commandSender;
        this.args = args;
    }

    @Override
    public boolean hasPermissionToExecuteCommand() {

        if (commandData.getPermission() == null) {
            throw new PermissionConfigException(String.format("Unable to find the permission for the command %s. Have you configured any permission at all?", commandData.getName()));
        }

        return commandSender.hasPermission(commandData.getPermission());

    }

    @Override
    public boolean canSenderExecuteCommand() {
        if (commandData.isPlayerOnly())
            return commandSender instanceof Player;
        return true;
    }

    @Override
    public boolean hasValidArgs() {

        if (commandData.getArguments() == null) {
            throw new ArgumentsConfigException(String.format("Unable to find arguments configuration for the command %s. Have you configured any arguments for the command?", commandData.getName()));
        }

        int mandatoryArgumentsCount = Arrays.stream(commandData.getArguments())
                .filter(commandArgument -> !commandArgument.optional())
                .toArray().length;

        if (args.length < mandatoryArgumentsCount) {
           return false;
        }

        for (CommandArgument argument : commandData.getArguments()) {
            int argumentPosition = argument.position();
            if (argumentPosition >= args.length) {
                if (!argument.optional()) {
                    return false;
                }
                continue;
            }

            // We can safely assume that the argument is present because of the check above
            String argumentAtPosition = args[argumentPosition];
            ICommandArgumentParser<?> expectedCommandArgument = new ArgumentParserFactory(argumentAtPosition)
                .generate(argument.validator());

            if (!expectedCommandArgument.isValid()) {
                return false;
            }
        }

        return true;

    }

    @Override
    public ICommandArgumentParser<?> getArgumentParser(int argumentPosition) {
        if (args.length <= argumentPosition) {
            throw new InvalidArgumentsException(String.format("The argument position %d is out of bounds for the command %s.", argumentPosition, commandData.getName()));
        }

        return Arrays.stream(commandData.getArguments())
                .filter(commandArgument -> commandArgument.position() == argumentPosition)
                .findFirst()
                .map(commandArgument -> new ArgumentParserFactory(args[argumentPosition]).generate(commandArgument.validator()))
                .orElseThrow();
    }

    @Override
    public boolean performAutoValidation(IAutoValidationConfiguration configuration) {
        if (commandData.getAutoValidationData() == null) {
            return true;
        }

        if (commandData.getAutoValidationData().validateSender()) {
            if (commandData.isPlayerOnly() && !(getCommandSender() instanceof Player)) {
                getCommandSender().sendMessage(configuration.getInvalidSenderMessage(this));
                return false;
            }
        }
        if (commandData.getAutoValidationData().validatePermission()) {
            if (!hasPermissionToExecuteCommand()) {
                getCommandSender().sendMessage(configuration.getNoPermissionMessage(this));
                return false;
            }
        }
        if (commandData.getAutoValidationData().validateArguments() && commandData.getArguments() != null) {
            if (!hasValidArgs()) {
                getCommandSender().sendMessage(configuration.getInvalidArgumentsMessage(this));
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked") // The cast is safe because it is of the same type as the argument class
    public <T> T getDependency(Class<T> dependencyClass) throws InvalidDependencyException {
        DependencyHandler dependencyHandler = DevCommand.getOrCreateInstance().getDependencyHandler();
        Object dependencyInstance = dependencyHandler.getDependencyInstance(commandData.getIntegration(), dependencyClass);
        if (dependencyInstance == null) {
            throw new InvalidDependencyException(String.format("Unable to find a dependency of type %s for the command %s. Have you configured the dependency correctly?", dependencyClass.getName(), commandData.getName()));
        }
        return (T) dependencyHandler.getDependencyInstance(commandData.getIntegration(), dependencyClass);
    }

}