package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.TestCommandDevCommand;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IntegrationTest {
    @Mock
    private CommandRegistry commandRegistry;
    @InjectMocks
    private CommandHandler commandHandler;
    private Integration pluginIntegration;

    @BeforeEach
    void setUp() {
        pluginIntegration = new Integration("myPlugin", "me.hgsoft.minecraft.devcommand");
        TestCommandDevCommand.called = false;
    }

    @AfterEach
    void tearDown() {
        TestCommandDevCommand.called = false;
        TestCommandDevCommand.args = null;
        TestCommandDevCommand.command = null;
        TestCommandDevCommand.sender = null;
        commandRegistry.setValues(pluginIntegration, null);
    }

    @Test
    void registerAndExecuteBukkitCommand() {

        BukkitCommandData bukkitCommand = new BukkitCommandDataBuilder("help bukkit", pluginIntegration, TestCommandDevCommand.class)
                .withDescription("Help Command")
                .withPermission("dev_commands.commands.help")
                .build();

        commandHandler.registerCommand(pluginIntegration, bukkitCommand);
        commandHandler.executeCommandByAlias(pluginIntegration, "help bukkit", null, "1");

        // The command was successfully called
        assertTrue(TestCommandDevCommand.called);

        // The sender is the same
        assertNull(TestCommandDevCommand.sender);
        // The args are also correct
        assertArrayEquals(new String[] {"1"}, TestCommandDevCommand.args);

        assertEquals(bukkitCommand, TestCommandDevCommand.command);

    }

    @Test
    void autoCommandDiscoveryAndExecuteBukkitCommand() {

        commandHandler.initCommandsAutoConfiguration(pluginIntegration);
        commandHandler.executeCommandByAlias(pluginIntegration, "test", null, "good", "afternoon");

        // The command was successfully called
        assertTrue(TestCommandDevCommand.called);

        // The sender is the same
        assertNull(TestCommandDevCommand.sender);
        // The args are also correct
        assertArrayEquals(new String[] {"good", "afternoon"}, TestCommandDevCommand.args);

        // The command alias is also correct
        assertNotNull(TestCommandDevCommand.command);
        assertEquals(TestCommandDevCommand.command.getAlias(), "test");

    }

}
