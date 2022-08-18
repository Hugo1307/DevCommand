package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.commands.BaseCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import me.hgsoft.minecraft.devcommand.utils.TestBaseCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseCommandHandlerTest {

    private CommandHandler commandHandler;
    private CommandRegistry commandRegistry;
    private BaseCommand baseCommandStub;
    private BukkitCommand bukkitCommandStub;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
        integrationStub = new Integration("myIntegration");
        baseCommandStub = new BaseCommand("test", "Test command", TestBaseCommand.class);
        bukkitCommandStub = new BukkitCommand("test", "Bukkit Test Command!", "", null, BukkitTestCommand.class);
    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integrationStub, null);
        TestBaseCommand.called = false;
        BukkitTestCommand.called = false;
    }

    @Test
    void registerCommand() {

        Assertions.assertNull(commandRegistry.getValues(integrationStub));

        commandHandler.registerCommand(integrationStub, baseCommandStub);

        Assertions.assertEquals(1, commandRegistry.getValues(integrationStub).size());
        Assertions.assertEquals(baseCommandStub, commandRegistry.getValues(integrationStub).get(0));

    }

    @Test
    void executeCommandByAlias_CommandNotRegistered() {

        Object[] commandArgs = new Object[1];
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, baseCommandStub.getAlias(), commandArgs);

        assertFalse(commandSuccessfullyExecuted);
        assertFalse(TestBaseCommand.called);

    }

    @Test
    void executeCommandByAliasForRegularCommand() {

        Object[] commandArgs = new Object[1];

        commandHandler.registerCommand(integrationStub, baseCommandStub);
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, baseCommandStub.getAlias(), commandArgs);

        assertTrue(commandSuccessfullyExecuted);
        assertTrue(TestBaseCommand.called);

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