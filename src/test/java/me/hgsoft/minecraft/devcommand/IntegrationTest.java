package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.TestCommandDevCommand;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    private static CommandHandler commandHandler;
    private static CommandRegistry commandRegistry;
    private static Integration pluginIntegration;

    @BeforeAll
    static void initialSetUp() {
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
        pluginIntegration = new Integration("myPlugin", "me.hgsoft.minecraft.devcommand");
    }

    @BeforeEach
    void setUp() {
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

        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("help bukkit", TestCommandDevCommand.class)
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
