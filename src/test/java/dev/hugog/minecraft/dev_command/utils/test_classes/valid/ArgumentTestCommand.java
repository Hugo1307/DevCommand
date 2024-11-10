package dev.hugog.minecraft.dev_command.utils.test_classes.valid;

import dev.hugog.minecraft.dev_command.annotations.Argument;
import dev.hugog.minecraft.dev_command.annotations.Arguments;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(alias = "test_arg", description = "Argument Test Command!", permission = "command.bukkit_test")
@Arguments(value = {
    @Argument(name = "string", description = "String to test", position = 0, parser = IntegerArgumentParser.class),
    @Argument(name = "number", description = "Number to test", position = 1, parser = IntegerArgumentParser.class, optional = true)
})
public class ArgumentTestCommand extends BukkitDevCommand {

    public static boolean called;
    public static boolean hasPermission;
    public static boolean hasValidArgs;

    public ArgumentTestCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("Argument Test Command Executed!");
        called = true;
        hasPermission = hasPermissionToExecuteCommand();
        hasValidArgs = hasValidArgs();
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return List.of();
    }

}
