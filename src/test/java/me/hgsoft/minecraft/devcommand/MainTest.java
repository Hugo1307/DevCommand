package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.commands.BaseCommand;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.commands.builder.BaseCommandBuilder;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import me.hgsoft.minecraft.devcommand.utils.TestBaseCommand;
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
        TestBaseCommand.called = false;
        BukkitTestCommand.called = false;
    }

    @AfterEach
    void tearDown() {
        TestBaseCommand.called = false;
        BukkitTestCommand.called = false;
        commandRegistry.setValues(pluginIntegration, null);
    }

    @Test
    void registerAndExecuteRegularCommand() {

        BaseCommand baseCommand = new BaseCommandBuilder("help", TestBaseCommand.class)
                .withDescription("Some Description")
                .build();

        commandHandler.registerCommand(pluginIntegration, baseCommand);
        commandHandler.executeCommandByAlias(pluginIntegration, "help", new Object[1]);

        Assertions.assertTrue(TestBaseCommand.called);

    }

    @Test
    void registerAndExecuteBukkitCommand() {

        BukkitCommand bukkitCommand = new BukkitCommandBuilder("help bukkit", BukkitTestCommand.class)
                .withDescription("Help Command")
                .withPermission("dev_commands.commands.help")
                .build();

        commandHandler.registerCommand(pluginIntegration, bukkitCommand);
        commandHandler.executeCommandByAlias(pluginIntegration, "help bukkit", new Object[]{null, new String[]{"1"}});

        Assertions.assertTrue(BukkitTestCommand.called);

    }

}
