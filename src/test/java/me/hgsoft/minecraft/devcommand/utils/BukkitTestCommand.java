package me.hgsoft.minecraft.devcommand.utils;

import me.hgsoft.minecraft.devcommand.annotations.ArgsValidation;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.executors.BukkitCommandExecutor;
import org.bukkit.command.CommandSender;

@Command(alias = "test", description = "Bukkit Test Command!")
@ArgsValidation(argsTypes = {Integer.class})
public class BukkitTestCommand extends BukkitCommandExecutor {

    public static boolean called;
    public static AbstractCommand command;
    public static CommandSender sender;
    public static String[] args;

    public BukkitTestCommand(AbstractCommand command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("Bukkit Test Command executed!");
        called = true;
        command = getCommand();
        sender = getCommandSender();
        args = getArgs();
    }

}
