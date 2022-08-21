package me.hgsoft.minecraft.devcommand.utils.arguments;

import me.hgsoft.minecraft.devcommand.annotations.ArgsValidation;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.executors.BukkitCommandExecutor;
import me.hgsoft.minecraft.devcommand.factories.validators.IntegerArgument;
import org.bukkit.command.CommandSender;

@Command(alias = "test_arg", description = "Argument Test Command!", permission = "command.bukkit_test")
@ArgsValidation(argsTypes = {IntegerArgument.class})
public class ArgumentTestCommand extends BukkitCommandExecutor {

    public static boolean called;
    public static boolean hasPermission;
    public static boolean hasValidArgs;

    public ArgumentTestCommand(BukkitCommand command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("Argument Test Command Executed!");
        called = true;
        hasPermission = hasPermissionToExecuteCommand();
        hasValidArgs = hasValidArgs();
    }

}
