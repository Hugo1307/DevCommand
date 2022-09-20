package me.hgsoft.minecraft.devcommand.commands;

import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.discovery.CommandDiscoveryService;
import me.hgsoft.minecraft.devcommand.exceptions.AutoConfigurationException;
import me.hgsoft.minecraft.devcommand.exceptions.InvalidIntegrationException;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.test_classes.valid.TestCommand;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandHandlerTest {

    @Mock
    private CommandRegistry commandRegistryMock;
    @Mock
    private Integration integrationMock;
    @InjectMocks
    private CommandHandler commandHandler;

    private BukkitCommandData bukkitCommandStub;

    @BeforeEach
    void setUp() {

        bukkitCommandStub = new BukkitCommandDataBuilder("test", integrationMock, TestCommand.class)
                .withDescription("Bukkit Test Command!")
                .withPermission("command.bukkit_test")
                .withMandatoryArguments(IntegerArgument.class)
                .build();

    }

    @AfterEach
    void tearDown() {
        commandRegistryMock.setValues(integrationMock, null);
        TestCommand.called = false;
    }

    @Test
    @DisplayName("Test CommandHandler registerCommand()")
    void registerCommand() {

        commandHandler.registerCommand(integrationMock, bukkitCommandStub);
        verify(commandRegistryMock, times(1)).add(integrationMock, bukkitCommandStub);

    }

    @Test
    @DisplayName("Test if command is executed when there are no commands registered.")
    void executeCommandByAlias_NoCommandsRegistered() {

        when(commandRegistryMock.getValues(integrationMock)).thenReturn(null);

        boolean commandExecuted = commandHandler.executeCommandByAlias(integrationMock, bukkitCommandStub.getAlias(), null, null);

        assertFalse(commandExecuted);

        verify(commandRegistryMock, times(1)).getValues(integrationMock);

    }

    @Test
    @DisplayName("Test if the command is executed when it is not registered.")
    void executeCommandByAlias_CommandNotRegistered() {

        AbstractCommandData abstractCommandDataMock = mock(AbstractCommandData.class);

        when(commandRegistryMock.getValues(integrationMock)).thenReturn(List.of(abstractCommandDataMock));
        when(abstractCommandDataMock.getAlias()).thenReturn("not_registered_alias");

        assertFalse(commandHandler.executeCommandByAlias(integrationMock, bukkitCommandStub.getAlias(), null, null));

        verify(commandRegistryMock, times(1)).getValues(integrationMock);

    }

    @Test
    @DisplayName("Test if command is executed successfully by its alias.")
    void executeCommandByAlias() {

        commandHandler.registerCommand(integrationMock, bukkitCommandStub);

        when(commandRegistryMock.getValues(integrationMock)).thenReturn(List.of(bukkitCommandStub));

        assertTrue(commandHandler.executeCommandByAlias(integrationMock, bukkitCommandStub.getAlias(), null, "good", "afternoon"));

        verify(commandRegistryMock, times(1)).getValues(integrationMock);

    }

    @Test
    @DisplayName("Test autoconfiguration method.")
    void initCommandsAutoConfiguration() {

        CommandDiscoveryService commandDiscoveryServiceMock = mock(CommandDiscoveryService.class);

        when(commandDiscoveryServiceMock.containsCommandsWithRepeatedAliases()).thenReturn(false);
        when(commandDiscoveryServiceMock.getDiscoveredCommandsData()).thenReturn(List.of(mock(AbstractCommandData.class)));
        when(integrationMock.isValid()).thenReturn(true);

        assertDoesNotThrow(() -> commandHandler.initCommandsAutoConfiguration(integrationMock, commandDiscoveryServiceMock));

        verify(commandRegistryMock, times(1)).add(any(), any());

    }

    @Test
    @DisplayName("Test autoconfiguration with duplicated command aliases.")
    void initCommandsAutoConfiguration_withDuplicatedAliases() {

        CommandDiscoveryService commandDiscoveryServiceMock = mock(CommandDiscoveryService.class);

        when(commandDiscoveryServiceMock.containsCommandsWithRepeatedAliases()).thenReturn(true);
        when(integrationMock.isValid()).thenReturn(true);

        assertThrows(AutoConfigurationException.class, () -> commandHandler.initCommandsAutoConfiguration(integrationMock, commandDiscoveryServiceMock));

        verify(commandDiscoveryServiceMock, times(1)).containsCommandsWithRepeatedAliases();

    }

    @Test
    @DisplayName("Test method to initiate commands autoconfiguration with Invalid integration.")
    void initCommandsAutoConfigurationWithInvalidIntegration() {

        Integration invalidIntegration = new Integration("MyPlugin", null);
        assertThrows(InvalidIntegrationException.class, () -> commandHandler.initCommandsAutoConfiguration(invalidIntegration));

    }

}