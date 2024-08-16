package dev.hugog.minecraft.dev_command.commands.executors;

import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.integration.Integration;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("unused")
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

        return commandHandler.executeCommand(integration, args, commandSender);
    }

}
