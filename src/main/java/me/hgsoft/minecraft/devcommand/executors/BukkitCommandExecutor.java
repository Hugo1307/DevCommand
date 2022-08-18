package me.hgsoft.minecraft.devcommand.executors;

import lombok.Generated;
import lombok.Getter;
import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import org.bukkit.command.CommandSender;

@Generated
@Getter
public abstract class BukkitCommandExecutor implements ICommandExecutor {

    private final AbstractCommand command;
    private final CommandSender commandSender;
    private final String[] args;

    public BukkitCommandExecutor(AbstractCommand command, CommandSender commandSender, String[] args) {
        this.command = command;
        this.commandSender = commandSender;
        this.args = args;
    }

}
