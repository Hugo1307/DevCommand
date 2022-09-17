package me.hgsoft.minecraft.devcommand.commands;

import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.exceptions.InvalidIntegrationException;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.TestCommandDevCommand;
import me.hgsoft.minecraft.devcommand.validators.IntegerArgument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandHandlerTest {

    @Mock
    private CommandRegistry commandRegistry;
    @Mock
    private Integration integration;
    @InjectMocks
    private CommandHandler commandHandler;

    private BukkitCommandData bukkitCommandStub;

    @BeforeEach
    void setUp() {

        bukkitCommandStub = new BukkitCommandDataBuilder("test", integration, TestCommandDevCommand.class)
                .withDescription("Bukkit Test Command!")
                .withPermission("command.bukkit_test")
                .withMandatoryArguments(IntegerArgument.class)
                .build();

    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integration, null);
        TestCommandDevCommand.called = false;
    }

    @Test
    @DisplayName("Test CommandHandler registerCommand()")
    void registerCommand() {

        commandHandler.registerCommand(integration, bukkitCommandStub);
        verify(commandRegistry, times(1)).add(integration, bukkitCommandStub);

    }

    @Test
    @DisplayName("Test if command is executed when there are no commands registered.")
    void executeCommandByAlias_NoCommandsRegistered() {

        when(commandRegistry.getValues(integration)).thenReturn(null);

        boolean commandExecuted = commandHandler.executeCommandByAlias(integration, bukkitCommandStub.getAlias(), null, null);

        assertFalse(commandExecuted);

        verify(commandRegistry, times(1)).getValues(integration);

    }

    @Test
    @DisplayName("Test if the command is executed when it is not registered.")
    void executeCommandByAlias_CommandNotRegistered() {

        AbstractCommandData abstractCommandDataMock = mock(AbstractCommandData.class);

        when(commandRegistry.getValues(integration)).thenReturn(List.of(abstractCommandDataMock));
        when(abstractCommandDataMock.getAlias()).thenReturn("not_registered_alias");

        assertFalse(commandHandler.executeCommandByAlias(integration, bukkitCommandStub.getAlias(), null, null));

        verify(commandRegistry, times(1)).getValues(integration);

    }

    @Test
    @DisplayName("Test if command is executed successfully by its alias.")
    void executeCommandByAlias() {

        commandHandler.registerCommand(integration, bukkitCommandStub);

        when(commandRegistry.getValues(integration)).thenReturn(List.of(bukkitCommandStub));

        assertTrue(commandHandler.executeCommandByAlias(integration, bukkitCommandStub.getAlias(), null, "good", "afternoon"));

        verify(commandRegistry, times(1)).getValues(integration);

    }

    @Test
    @DisplayName("Test method to initiate commands autoconfiguration.")
    void initCommandsAutoConfiguration() {

        commandHandler.initCommandsAutoConfiguration(integration);

        assertThat(commandRegistry.getValues(integration))
                .isNotNull()
                .hasSize(2)
                .contains(bukkitCommandStub);

    }

    @Test
    @DisplayName("Test method to initiate commands autoconfiguration with Invalid integration.")
    void initCommandsAutoConfigurationWithInvalidIntegration() {

        Integration invalidIntegration = new Integration("MyPlugin", null);
        assertThrows(InvalidIntegrationException.class, () -> commandHandler.initCommandsAutoConfiguration(invalidIntegration));

    }

}