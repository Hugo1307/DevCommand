package dev.hugog.minecraft.dev_command;

import dev.hugog.minecraft.dev_command.commands.builder.BukkitCommandDataBuilder;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.TestCommand;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.commands.CommandRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertEquals(bukkitCommand, TestCommand.command);

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
        Assertions.assertEquals(TestCommand.command.getAlias(), "test");

    }

}
