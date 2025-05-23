package dev.hugog.minecraft.dev_command.utils.test_classes.invalid;

import dev.hugog.minecraft.dev_command.annotations.Argument;
import dev.hugog.minecraft.dev_command.annotations.Arguments;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(alias = "test", description = "Bukkit Test Command!", permission = "command.bukkit_test")
@Arguments(value = {
    @Argument(name = "string", description = "String to test", position = 0, parser = StringArgumentParser.class),
    @Argument(name = "number", description = "Number to test", position = 1, parser = IntegerArgumentParser.class, optional = true)
})
public class TestCommandCopy extends BukkitDevCommand {

    public static boolean called;
    public static BukkitCommandData command;
    public static CommandSender sender;
    public static String[] args;

    public TestCommandCopy(BukkitCommandData command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("Bukkit Test Command executed!");
        called = true;
        command = this.getCommandData();
        sender = getCommandSender();
        args = getArgs();
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return List.of();
    }

}
