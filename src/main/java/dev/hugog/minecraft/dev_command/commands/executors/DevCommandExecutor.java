package dev.hugog.minecraft.dev_command.commands.executors;

import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@SuppressWarnings("unused")
public final class DevCommandExecutor implements CommandExecutor, TabCompleter {

    private final String commandLabel;
    private final Integration integration;
    private final DevCommand devCommand;

    public DevCommandExecutor(String commandLabel, Integration integration) {
        this.commandLabel = commandLabel;
        this.integration = integration;
        this.devCommand = DevCommand.getOrCreateInstance();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        boolean isAnAlias = command.getAliases().stream().anyMatch(s -> s.equalsIgnoreCase(commandLabel));
        if (!command.getLabel().equalsIgnoreCase(commandLabel) && !isAnAlias) { // Not our command!
            return false;
        }
        return devCommand.getCommandHandler().executeCommand(integration, args, commandSender);
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        boolean isAnAlias = command.getAliases().stream().anyMatch(s -> s.equalsIgnoreCase(commandLabel));
        if (!command.getLabel().equalsIgnoreCase(commandLabel) && !isAnAlias) { // Not our command!
            return null;
        }
        return devCommand.getCommandHandler().executeTabComplete(integration, commandSender, args);
    }
}
