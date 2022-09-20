package me.hgsoft.minecraft.devcommand.commands;

import lombok.Generated;
import lombok.Getter;
import me.hgsoft.minecraft.devcommand.DevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.dependencies.DependencyHandler;
import me.hgsoft.minecraft.devcommand.exceptions.ArgumentsConfigException;
import me.hgsoft.minecraft.devcommand.exceptions.PermissionConfigException;
import me.hgsoft.minecraft.devcommand.factories.ArgumentFactory;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.validators.CommandArgument;
import me.hgsoft.minecraft.devcommand.validators.ICommandArgument;
import org.bukkit.command.CommandSender;

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
    public boolean hasValidArgs() {

        if (commandData.getMandatoryArguments() == null && commandData.getOptionalArguments() == null) {
            throw new ArgumentsConfigException(String.format("Unable to find arguments configuration for the command %s. Have you added configured any arguments for the command?", commandData.getName()));
        }

        int mandatoryArgumentsCount = commandData.getMandatoryArguments() != null ? commandData.getMandatoryArguments().length : 0;
        int optionalArgumentsCount = commandData.getOptionalArguments() != null ? commandData.getOptionalArguments().length : 0;

        if (args.length < mandatoryArgumentsCount) {
           return false;
        }

        String[] mandatoryArguments = Arrays.copyOfRange(args, 0, mandatoryArgumentsCount);

        for (int mandatoryArgumentIdx = 0; mandatoryArgumentIdx < mandatoryArguments.length; mandatoryArgumentIdx++) {

            String currentArgument = mandatoryArguments[mandatoryArgumentIdx];
            Class<? extends CommandArgument<?>> expectedCommandArgumentClass = commandData.getMandatoryArguments()[mandatoryArgumentIdx];
            ICommandArgument<?> expectedCommandArgument = new ArgumentFactory(currentArgument).generate(expectedCommandArgumentClass);

            if (!expectedCommandArgument.isValid()) {
                return false;
            }

        }

        String[] optionalArguments = Arrays.copyOfRange(args, mandatoryArgumentsCount, Math.min(mandatoryArgumentsCount+optionalArgumentsCount, args.length));

        for (int optionalArgumentIdx = 0; optionalArgumentIdx < optionalArguments.length; optionalArgumentIdx++) {

            if (optionalArgumentIdx < optionalArgumentsCount) {

                String currentArgument = optionalArguments[optionalArgumentIdx];
                Class<? extends CommandArgument<?>> expectedCommandArgumentClass = commandData.getOptionalArguments()[optionalArgumentIdx];
                ICommandArgument<?> expectedCommandArgument = new ArgumentFactory(currentArgument).generate(expectedCommandArgumentClass);

                if (!expectedCommandArgument.isValid()) {
                    return false;
                }

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

}