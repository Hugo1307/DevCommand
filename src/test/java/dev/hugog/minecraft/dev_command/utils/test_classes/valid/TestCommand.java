package dev.hugog.minecraft.dev_command.utils.test_classes.valid;

import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.validators.IntegerArgument;
import org.bukkit.command.CommandSender;

@Command(alias = "test", description = "Bukkit Test Command!", permission = "command.bukkit_test")
@ArgsValidation(mandatoryArgs = {IntegerArgument.class})
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
