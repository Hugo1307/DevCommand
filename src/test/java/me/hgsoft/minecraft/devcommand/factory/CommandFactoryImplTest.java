package me.hgsoft.minecraft.devcommand.factory;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandFactoryImplTest {

    private CommandFactory bukkitCommandFactory;
    private BukkitCommand bukkitCommand;

    @BeforeEach
    void setUp() {
        bukkitCommandFactory = new CommandFactoryImpl(null, "boa", "tarde");
        bukkitCommand = new BukkitCommand("testBukkit", "Test Bukkit Command", "", null, BukkitTestCommand.class);
    }

    @Test
    void generateExecutorForBukkitCommand() {
        ICommandExecutor generatedBukkitCommandExecutor = bukkitCommandFactory.generateExecutor(bukkitCommand);
        Assertions.assertTrue(generatedBukkitCommandExecutor instanceof BukkitTestCommand);
    }

}