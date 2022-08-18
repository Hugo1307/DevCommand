package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import me.hgsoft.minecraft.devcommand.utils.TestCommand;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MainTest {

    private static CommandHandler commandHandler;
    private static CommandRegistry commandRegistry;
    private static Integration pluginIntegration;

    @BeforeAll
    static void initialSetUp() {
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
        pluginIntegration = new Integration("myPlugin");
    }

    @BeforeEach
    void setUp() {
        TestCommand.called = false;
        BukkitTestCommand.called = false;
    }

    @AfterEach
    void tearDown() {
        TestCommand.called = false;
        BukkitTestCommand.called = false;
        commandRegistry.setValues(pluginIntegration, null);
    }

    @Test
    void registerAndExecuteRegularCommand() {

        commandHandler.registerCommand(pluginIntegration, new Command("help", "Some description", TestCommand.class));
        commandHandler.executeCommandByAlias(pluginIntegration, "help", new Object[1]);

        Assertions.assertTrue(TestCommand.called);

    }

    @Test
    void registerAndExecuteBukkitCommand() {

        commandHandler.registerCommand(pluginIntegration, new BukkitCommand("bukkit help", "Help command.", "commands.help", null, BukkitTestCommand.class));
        commandHandler.executeCommandByAlias(pluginIntegration, "bukkit help", new Object[] {null, new String[] {"1"}});

    }

}
