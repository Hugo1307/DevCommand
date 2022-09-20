package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.test_classes.valid.TestCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("all")
class IntegrationTestIT {

    private CommandRegistry commandRegistry;
    private CommandHandler commandHandler;
    private Integration pluginIntegration;

    @BeforeEach
    void setUp() {
        commandRegistry = new CommandRegistry();
        commandHandler = new CommandHandler(commandRegistry);
        pluginIntegration = new Integration("myPlugin", "me.hgsoft.minecraft.devcommand.utils.test_classes.valid");
        TestCommand.called = false;
    }

    @AfterEach
    void tearDown() {
        TestCommand.called = false;
        TestCommand.args = null;
        TestCommand.command = null;
        TestCommand.sender = null;
        commandRegistry.setValues(pluginIntegration, null);
    }

    @Test
    void registerAndExecuteBukkitCommand() {

        BukkitCommandData bukkitCommand = new BukkitCommandDataBuilder("test", pluginIntegration, TestCommand.class)
                .withDescription("Help Command")
                .withPermission("dev_commands.commands.help")
                .build();

        commandHandler.registerCommand(pluginIntegration, bukkitCommand);
        commandHandler.executeCommandByAlias(pluginIntegration, "test", null, "1");

        // The command was successfully called
        assertTrue(TestCommand.called);

        // The sender is the same
        assertNull(TestCommand.sender);
        // The args are also correct
        assertArrayEquals(new String[] {"1"}, TestCommand.args);

        assertEquals(bukkitCommand, TestCommand.command);

    }

    @Test
    void autoCommandDiscoveryAndExecuteBukkitCommand() {

        commandHandler.initCommandsAutoConfiguration(pluginIntegration);
        commandHandler.executeCommandByAlias(pluginIntegration, "test", null, "good", "afternoon");

        // The command was successfully called
        assertTrue(TestCommand.called);

        // The sender is the same
        assertNull(TestCommand.sender);
        // The args are also correct
        assertArrayEquals(new String[] {"good", "afternoon"}, TestCommand.args);

        // The command alias is also correct
        assertNotNull(TestCommand.command);
        assertEquals(TestCommand.command.getAlias(), "test");

    }

}
