package dev.hugog.minecraft.dev_command.utils.test_classes.valid;

import dev.hugog.minecraft.dev_command.annotations.Argument;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import org.bukkit.command.CommandSender;

@Command(alias = "test", description = "Bukkit Test Command!", permission = "command.bukkit_test")
@ArgsValidation(value = {
    @Argument(name = "string", description = "String to test", position = 0, validator = StringArgumentParser.class),
    @Argument(name = "number", description = "Number to test", position = 1, validator = IntegerArgumentParser.class, optional = true)
})
public class TestCommand extends BukkitDevCommand {

    public static boolean called;
    public static BukkitCommandData command;
    public static CommandSender sender;
    public static String[] args;

    public TestCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
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

}
