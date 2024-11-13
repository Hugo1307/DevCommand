package dev.hugog.minecraft.dev_command;

import dev.hugog.minecraft.dev_command.commands.builder.BukkitCommandDataBuilder;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.CompoundedAliasCommand;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.EmptyAliasCommand;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.TestCommand;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.commands.CommandRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTestIT {

    private CommandRegistry commandRegistry;
    private CommandHandler commandHandler;
    private Integration pluginIntegration;

    @BeforeEach
    void setUp() {
        commandRegistry = new CommandRegistry();
        commandHandler = new CommandHandler(commandRegistry);
        pluginIntegration = new Integration("myPlugin", "dev.hugog.minecraft.dev_command.utils.test_classes.valid");
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
        commandHandler.executeCommand(pluginIntegration, null, new String[]{"test", "1"});

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
        commandHandler.executeCommand(pluginIntegration, null, new String[]{"test", "good", "afternoon"});

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

    @Test
    void autoDiscovery_And_ExecuteBukkitCommandWithEmptyAlias() {
        commandHandler.initCommandsAutoConfiguration(pluginIntegration);
        commandHandler.executeCommand(pluginIntegration, null, new String[]{"", "good", "afternoon"});

        assertTrue(EmptyAliasCommand.called);
        assertNull(EmptyAliasCommand.sender);

        assertArrayEquals(new String[] {"good", "afternoon"}, EmptyAliasCommand.args);

        assertNotNull(EmptyAliasCommand.command);

        assertEquals(EmptyAliasCommand.command.getAlias(), "");
    }

    @Test
    void autoDiscovery_And_ExecuteBukkitCommandWithMultipleAlias() {
        commandHandler.initCommandsAutoConfiguration(pluginIntegration);
        commandHandler.executeCommand(pluginIntegration, null, new String[]{"multiple", "aliases", "good", "afternoon"});

        assertTrue(CompoundedAliasCommand.called);
        assertNull(CompoundedAliasCommand.sender);

        assertArrayEquals(new String[] {"good", "afternoon"}, CompoundedAliasCommand.args);

        assertNotNull(CompoundedAliasCommand.command);

        assertEquals(CompoundedAliasCommand.command.getAlias(), "multiple aliases");
    }

}
