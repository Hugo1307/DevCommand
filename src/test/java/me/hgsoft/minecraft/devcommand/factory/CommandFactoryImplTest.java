package me.hgsoft.minecraft.devcommand.factory;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import me.hgsoft.minecraft.devcommand.utils.TestCommand;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandFactoryImplTest {

    private CommandFactory regularCommandFactory;
    private CommandFactory bukkitCommandFactory;
    private Command command;
    private BukkitCommand bukkitCommand;

    @BeforeEach
    void setUp() {

        regularCommandFactory = new CommandFactoryImpl((Object) null);
        bukkitCommandFactory = new CommandFactoryImpl(null, new String[] {"boa", "tarde"});

        command = new Command("test", "Test Command", TestCommand.class);
        bukkitCommand = new BukkitCommand("testBukkit", "Test Bukkit Command", "", null, BukkitTestCommand.class);

    }

    @Test
    void generateExecutorForRegularCommand() {
        ICommandExecutor generatedCommandExecutor = regularCommandFactory.generateExecutor(command);
        Assertions.assertTrue(generatedCommandExecutor instanceof TestCommand);
    }

    @Test
    void generateExecutorForBukkitCommand() {
        ICommandExecutor generatedBukkitCommandExecutor = bukkitCommandFactory.generateExecutor(bukkitCommand);
        Assertions.assertTrue(generatedBukkitCommandExecutor instanceof BukkitTestCommand);
    }

}