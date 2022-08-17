package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.utils.TestCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MainTest {

    @BeforeEach
    void setUp() {
        TestCommand.called = false;
    }

    @AfterEach
    void tearDown() {
        TestCommand.called = false;
    }

    @Test
    void mainTest() {

        CommandHandler commandHandler = CommandHandler.createOrGetInstance();
        Integration pluginIntegration = new Integration("myPlugin");

        commandHandler.registerCommand(pluginIntegration, new Command("help", "Some description", TestCommand.class));
        commandHandler.executeCommandByAlias(pluginIntegration, "help", new Object[1]);

        Assertions.assertTrue(TestCommand.called);

    }

}
