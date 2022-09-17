package me.hgsoft.minecraft.devcommand.commands.executors;

import lombok.NonNull;
import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.DevCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public final class DevCommandExecutor implements CommandExecutor {

    private final String commandLabel;
    private final Integration integration;

    public DevCommandExecutor(String commandLabel, Integration integration) {
        this.commandLabel = commandLabel;
        this.integration = integration;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {

        boolean isAnAlias = command.getAliases().stream().anyMatch(s -> s.equalsIgnoreCase(commandLabel));

        // Not our command!
        if (!command.getLabel().equalsIgnoreCase(commandLabel) && !isAnAlias) {
            return false;
        }

        DevCommand devCommand = DevCommand.getOrCreateInstance();
        CommandHandler commandHandler = devCommand.getCommandHandler();

        // No arguments for the command.
        if (args.length == 0) {
            commandHandler.executeCommandByAlias(integration, "", commandSender);
            return true;
        } else if (args.length == 1) {
            commandHandler.executeCommandByAlias(integration, args[0], commandSender);
            return true;
        } else {
            commandHandler.executeCommandByAlias(integration, args[0], commandSender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }

    }

}
