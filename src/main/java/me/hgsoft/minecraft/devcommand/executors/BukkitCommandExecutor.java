package me.hgsoft.minecraft.devcommand.executors;

import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public abstract class BukkitCommandExecutor implements ICommandExecutor {

    private final CommandSender commandSender;
    private final String[] args;

    public BukkitCommandExecutor(CommandSender commandSender, String[] args) {
        this.commandSender = commandSender;
        this.args = args;
    }

}
