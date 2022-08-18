package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    private CommandHandler commandHandler;
    private CommandRegistry commandRegistry;
    private BukkitCommand bukkitCommandStub;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
        integrationStub = new Integration("myIntegration", null);
        bukkitCommandStub = new BukkitCommand("test", "Bukkit Test Command!", "", null, BukkitTestCommand.class);
    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integrationStub, null);
        BukkitTestCommand.called = false;
    }

    @Test
    void registerCommand() {

        Assertions.assertNull(commandRegistry.getValues(integrationStub));

        commandHandler.registerCommand(integrationStub, bukkitCommandStub);

        Assertions.assertEquals(1, commandRegistry.getValues(integrationStub).size());
        Assertions.assertEquals(bukkitCommandStub, commandRegistry.getValues(integrationStub).get(0));

    }

    @Test
    void executeCommandByAlias_CommandNotRegistered() {

        Object[] commandArgs = new Object[1];
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, bukkitCommandStub.getAlias(), commandArgs);

        assertFalse(commandSuccessfullyExecuted);
        assertFalse(BukkitTestCommand.called);

    }

    @Test
    void executeCommandByAliasForBukkitCommand() {

        Object[] commandArgs = new Object[] {null, new String[] {"boa", "tarde"}};

        commandHandler.registerCommand(integrationStub, bukkitCommandStub);
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, bukkitCommandStub.getAlias(), commandArgs);

        assertTrue(commandSuccessfullyExecuted);
        assertTrue(BukkitTestCommand.called);

    }

}