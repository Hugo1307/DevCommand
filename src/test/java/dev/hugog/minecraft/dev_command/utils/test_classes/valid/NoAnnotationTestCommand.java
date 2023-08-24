package dev.hugog.minecraft.dev_command.utils.test_classes.valid;

import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;

public class NoAnnotationTestCommand extends BukkitDevCommand {

    public static boolean called;
    public static boolean hasPermission;
    public static boolean hasValidArgs;

    public NoAnnotationTestCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
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
