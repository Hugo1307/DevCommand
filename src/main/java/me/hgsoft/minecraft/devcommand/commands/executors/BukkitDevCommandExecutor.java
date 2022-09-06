package me.hgsoft.minecraft.devcommand.commands.executors;

import lombok.Generated;
import lombok.Getter;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.exceptions.ArgumentsConfigException;
import me.hgsoft.minecraft.devcommand.exceptions.PermissionConfigException;
import me.hgsoft.minecraft.devcommand.factories.ArgumentFactory;
import me.hgsoft.minecraft.devcommand.validators.CommandArgument;
import me.hgsoft.minecraft.devcommand.validators.ICommandArgument;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@Generated
@Getter
public abstract class BukkitDevCommandExecutor implements IDevCommandExecutor {

    private final BukkitCommandData command;
    private final CommandSender commandSender;
    private final String[] args;

    public BukkitDevCommandExecutor(BukkitCommandData command, CommandSender commandSender, String[] args) {
        this.command = command;
        this.commandSender = commandSender;
        this.args = args;
    }

    @Override
    public boolean hasPermissionToExecuteCommand() {
        if (command.getPermission() == null) {
            throw new PermissionConfigException(String.format("Unable to find the permission for the command %s. Have you configured any permission at all?", command.getName()));
        }
        return commandSender.hasPermission(command.getPermission());
    }

    @Override
    public boolean hasValidArgs() {

        if (command.getMandatoryArguments() == null && command.getOptionalArguments() == null) {
            throw new ArgumentsConfigException(String.format("Unable to find arguments configuration for the command %s. Have you added configured any arguments for the command?", command.getName()));
        }

        int mandatoryArgumentsCount = command.getMandatoryArguments() != null ? command.getMandatoryArguments().length : 0;
        int optionalArgumentsCount = command.getOptionalArguments() != null ? command.getOptionalArguments().length : 0;

        if (args.length < mandatoryArgumentsCount) {
           return false;
        }

        String[] mandatoryArguments = Arrays.copyOfRange(args, 0, mandatoryArgumentsCount);

        for (int mandatoryArgumentIdx = 0; mandatoryArgumentIdx < mandatoryArguments.length; mandatoryArgumentIdx++) {

            String currentArgument = mandatoryArguments[mandatoryArgumentIdx];
            Class<? extends CommandArgument<?>> expectedCommandArgumentClass = command.getMandatoryArguments()[mandatoryArgumentIdx];
            ICommandArgument<?> expectedCommandArgument = new ArgumentFactory(currentArgument).generate(expectedCommandArgumentClass);

            if (!expectedCommandArgument.isValid()) {
                return false;
            }

        }

        String[] optionalArguments = Arrays.copyOfRange(args, mandatoryArgumentsCount, Math.min(mandatoryArgumentsCount+optionalArgumentsCount, args.length));

        for (int optionalArgumentIdx = 0; optionalArgumentIdx < optionalArguments.length; optionalArgumentIdx++) {

            if (optionalArgumentIdx < optionalArgumentsCount) {

                String currentArgument = optionalArguments[optionalArgumentIdx];
                Class<? extends CommandArgument<?>> expectedCommandArgumentClass = command.getOptionalArguments()[optionalArgumentIdx];
                ICommandArgument<?> expectedCommandArgument = new ArgumentFactory(currentArgument).generate(expectedCommandArgumentClass);

                if (!expectedCommandArgument.isValid()) {
                    return false;
                }

            }

        }

        return true;

    }

}