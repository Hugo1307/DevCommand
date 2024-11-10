package dev.hugog.minecraft.dev_command.utils.test_classes.valid;

import dev.hugog.minecraft.dev_command.annotations.Argument;
import dev.hugog.minecraft.dev_command.annotations.Arguments;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(alias = "multiple aliases", description = "Bukkit Compounded Test Command!", permission = "command.bukkit_test")
@Arguments(value = {
    @Argument(name = "string", description = "String to test", position = 0, parser = StringArgumentParser.class),
    @Argument(name = "number", description = "Number to test", position = 1, parser = IntegerArgumentParser.class, optional = true)
})
public class CompoundedAliasCommand extends BukkitDevCommand {

    public static boolean called;
    public static BukkitCommandData command;
    public static CommandSender sender;
    public static String[] args;

    public CompoundedAliasCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {
        System.out.println("Bukkit Compounded Test Command executed!");
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
