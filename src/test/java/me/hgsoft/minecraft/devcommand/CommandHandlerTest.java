package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.TestCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    private CommandHandler commandHandler;
    private CommandRegistry commandRegistry;
    private Command commandStub;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
        integrationStub = new Integration("myIntegration");
        commandStub = new Command("test", "Test command", TestCommand.class);
    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integrationStub, null);
        TestCommand.called = false;
    }

    @Test
    void registerCommand() {

        Assertions.assertNull(commandRegistry.getValues(integrationStub));

        commandHandler.registerCommand(integrationStub, commandStub);

        Assertions.assertEquals(1, commandRegistry.getValues(integrationStub).size());
        Assertions.assertEquals(commandStub, commandRegistry.getValues(integrationStub).get(0));

    }

    @Test
    void executeCommandByAlias_CommandNotRegistered() {

        Object[] commandArgs = new Object[1];
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, commandStub.getAlias(), commandArgs);

        assertFalse(commandSuccessfullyExecuted);
        assertFalse(TestCommand.called);

    }

    @Test
    void executeCommandByAlias() {

        Object[] commandArgs = new Object[1];

        commandHandler.registerCommand(integrationStub, commandStub);
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, commandStub.getAlias(), commandArgs);

        assertTrue(commandSuccessfullyExecuted);
        assertTrue(TestCommand.called);

    }

}