package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import lombok.Generated;
import lombok.Getter;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.dependencies.DependencyHandler;
import dev.hugog.minecraft.dev_command.exceptions.ArgumentsConfigException;
import dev.hugog.minecraft.dev_command.exceptions.PermissionConfigException;
import dev.hugog.minecraft.dev_command.factories.ArgumentValidatorFactory;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.arguments.validators.ICommandArgumentValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                .filter(commandArgument -> !commandArgument.isOptional())
                .toArray().length;

        if (args.length < mandatoryArgumentsCount) {
           return false;
        }

        for (CommandArgument argument : commandData.getArguments()) {
            int argumentPosition = argument.getPosition();
            if (argumentPosition >= args.length) {
                if (!argument.isOptional()) {
                    return false;
                }
                continue;
            }

            // We can safely assume that the argument is present because of the check above
            String argumentAtPosition = args[argumentPosition];
            ICommandArgumentValidator<?> expectedCommandArgument = new ArgumentValidatorFactory(argumentAtPosition)
                .generate(argument.getValidator());

            if (!expectedCommandArgument.isValid()) {
                return false;
            }
        }

        return true;

    }

    @Override
    public List<Object> getDependencies() {

        DependencyHandler dependencyHandler = DevCommand.getOrCreateInstance().getDependencyHandler();
        Integration commandIntegration = commandData.getIntegration();

        return Arrays.stream(getCommandData().getDependencies())
                .map(dependencyClass -> dependencyHandler.getDependencyInstance(commandIntegration, dependencyClass))
                .collect(Collectors.toList());

    }

    @Override
    public Object getDependency(Class<?> dependencyClass) {
        DependencyHandler dependencyHandler = DevCommand.getOrCreateInstance().getDependencyHandler();
        return dependencyHandler.getDependencyInstance(commandData.getIntegration(), dependencyClass);
    }

}