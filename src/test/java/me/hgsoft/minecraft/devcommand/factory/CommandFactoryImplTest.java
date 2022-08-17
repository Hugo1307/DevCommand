package me.hgsoft.minecraft.devcommand.factory;

import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.executors.CommandExecutor;
import me.hgsoft.minecraft.devcommand.utils.TestCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryImplTest {

    private CommandFactory commandFactory;
    private Command command;

    @BeforeEach
    void setUp() {
        commandFactory = new CommandFactoryImpl(new Object[1]);
        command = new Command("test", "Test Command", TestCommand.class);
    }

    @Test
    void generateExecutor() {
        CommandExecutor generatedCommandExecutor = commandFactory.generateExecutor(command);
        Assertions.assertTrue(generatedCommandExecutor instanceof TestCommand);
    }

}