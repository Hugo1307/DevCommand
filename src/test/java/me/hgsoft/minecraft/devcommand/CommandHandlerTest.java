package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.exceptions.InvalidIntegrationException;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.TestCommandDevCommand;
import me.hgsoft.minecraft.devcommand.validators.IntegerArgument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    private CommandHandler commandHandler;
    private CommandRegistry commandRegistry;
    private BukkitCommandData bukkitCommandStub;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
        integrationStub = new Integration("myIntegration", "me.hgsoft.");
        bukkitCommandStub = new BukkitCommandDataBuilder("test", integrationStub, TestCommandDevCommand.class)
                .withDescription("Bukkit Test Command!")
                .withPermission("command.bukkit_test")
                .withMandatoryArguments(IntegerArgument.class)
                .build();
    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integrationStub, null);
        TestCommandDevCommand.called = false;
    }

    @Test
    void registerCommand() {

        Assertions.assertNull(commandRegistry.getValues(integrationStub));

        commandHandler.registerCommand(integrationStub, bukkitCommandStub);

        Assertions.assertEquals(1, commandRegistry.getValues(integrationStub).size());
        Assertions.assertEquals(bukkitCommandStub, commandRegistry.getValues(integrationStub).get(0));

    }

    @Test
    void executeCommandByAlias_CommandNotRegistered() {

        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, bukkitCommandStub.getAlias(), null, "1");

        assertFalse(commandSuccessfullyExecuted);
        assertFalse(TestCommandDevCommand.called);

    }

    @Test
    void executeCommandByAliasForBukkitCommand() {

        commandHandler.registerCommand(integrationStub, bukkitCommandStub);
        boolean commandSuccessfullyExecuted = commandHandler.executeCommandByAlias(integrationStub, bukkitCommandStub.getAlias(), null, "good", "afternoon");

        assertTrue(commandSuccessfullyExecuted);
        assertTrue(TestCommandDevCommand.called);

    }

    @Test
    void initCommandsAutoConfiguration() {

        commandHandler.initCommandsAutoConfiguration(integrationStub);

        assertThat(commandRegistry.getValues(integrationStub))
                .isNotNull()
                .hasSize(2)
                .contains(bukkitCommandStub);

    }

    @Test
    void initCommandsAutoConfigurationWithInvalidIntegration() {

        Integration invalidIntegration = new Integration("MyPlugin", null);
        assertThrows(InvalidIntegrationException.class, () -> commandHandler.initCommandsAutoConfiguration(invalidIntegration));

    }

}