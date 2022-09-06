package me.hgsoft.minecraft.devcommand.utils.arguments;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.executors.BukkitDevCommandExecutor;
import org.bukkit.command.CommandSender;

public class NoAnnotationArgumentTestCommand extends BukkitDevCommandExecutor {

    public static boolean called;
    public static boolean hasPermission;
    public static boolean hasValidArgs;

    public NoAnnotationArgumentTestCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("No Annotation Argument Test Command Executed!");
        called = true;
        hasPermission = hasPermissionToExecuteCommand();
        hasValidArgs = hasValidArgs();
    }

}
