package me.hgsoft.minecraft.devcommand.commands.executors;

import lombok.NonNull;
import me.hgsoft.minecraft.devcommand.CommandHandler;
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

        // No arguments for the command.
        if (args.length <= 0) {
            // TODO: Help Message.
            return true;
        } else if (args.length == 1) {
            CommandHandler commandHandler = CommandHandler.createOrGetInstance();
            commandHandler.executeCommandByAlias(integration, args[0], commandSender);
            return true;
        } else {
            CommandHandler commandHandler = CommandHandler.createOrGetInstance();
            commandHandler.executeCommandByAlias(integration, args[0], commandSender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }

    }

}
