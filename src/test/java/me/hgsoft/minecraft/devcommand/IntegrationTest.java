package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
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
        BukkitTestCommand.called = false;
    }

    @AfterEach
    void tearDown() {
        BukkitTestCommand.called = false;
        BukkitTestCommand.args = null;
        BukkitTestCommand.command = null;
        BukkitTestCommand.sender = null;
        commandRegistry.setValues(pluginIntegration, null);
    }

    @Test
    void registerAndExecuteBukkitCommand() {

        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("help bukkit", BukkitTestCommand.class)
                .withDescription("Help Command")
                .withPermission("dev_commands.commands.help")
                .build();

        commandHandler.registerCommand(pluginIntegration, bukkitCommand);
        commandHandler.executeCommandByAlias(pluginIntegration, "help bukkit", null, "1");

        // The command was successfully called
        assertTrue(BukkitTestCommand.called);

        // The sender is the same
        assertNull(BukkitTestCommand.sender);
        // The args are also correct
        assertArrayEquals(new String[] {"1"}, BukkitTestCommand.args);

        assertEquals(bukkitCommand, BukkitTestCommand.command);

    }

    @Test
    void autoCommandDiscoveryAndExecuteBukkitCommand() {

        commandHandler.initCommandsAutoConfiguration(pluginIntegration);
        commandHandler.executeCommandByAlias(pluginIntegration, "test", null, "good", "afternoon");

        // The command was successfully called
        assertTrue(BukkitTestCommand.called);

        // The sender is the same
        assertNull(BukkitTestCommand.sender);
        // The args are also correct
        assertArrayEquals(new String[] {"good", "afternoon"}, BukkitTestCommand.args);

        // The command alias is also correct
        assertNotNull(BukkitTestCommand.command);
        assertEquals(BukkitTestCommand.command.getAlias(), "test");

    }

}
