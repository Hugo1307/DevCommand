package dev.hugog.minecraft.dev_command.registry;

import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.commands.CommandRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class CommandRegistryTest {

    @Mock
    private Integration integrationStub;
    @Mock
    private BukkitCommandData bukkitCommand;

    @Test
    void add() {

        CommandRegistry commandRegistry = spy(CommandRegistry.class);

        assertNull(commandRegistry.getValues(integrationStub));

        commandRegistry.add(integrationStub, bukkitCommand);

        assertNotNull(commandRegistry.getValues(integrationStub));
        assertEquals(1, commandRegistry.getValues(integrationStub).size());
        assertEquals(bukkitCommand, commandRegistry.getValues(integrationStub).get(0));

    }

    @Test
    void remove() {

        CommandRegistry commandRegistry = spy(CommandRegistry.class);

        commandRegistry.setValues(integrationStub, List.of(bukkitCommand));

        assertNotNull(commandRegistry.getValues(integrationStub));
        assertEquals(1, commandRegistry.getValues(integrationStub).size());
        assertEquals(bukkitCommand, commandRegistry.getValues(integrationStub).get(0));

    }

}