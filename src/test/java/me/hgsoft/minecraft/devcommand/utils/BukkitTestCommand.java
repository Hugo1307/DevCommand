package me.hgsoft.minecraft.devcommand.utils;

import me.hgsoft.minecraft.devcommand.annotations.ArgsValidation;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.executors.BukkitCommandExecutor;
import org.bukkit.command.CommandSender;

@Command(alias = "test")
@ArgsValidation(argsTypes = {Integer.class})
public class BukkitTestCommand extends BukkitCommandExecutor {

    public static boolean called;

    public BukkitTestCommand(CommandSender commandSender, String[] args) {
        super(commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("Bukkit Test Command executed!");
        called = true;
    }

}
