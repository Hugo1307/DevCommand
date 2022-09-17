package me.hgsoft.minecraft.devcommand.registry;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.TestCommandDevCommand;
import org.junit.jupiter.api.*;

import java.util.List;

class CommandRegistryTest {

    private BukkitCommandData bukkitCommand;
    private CommandRegistry commandRegistry;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        integrationStub = new Integration("myIntegration", null);
        commandRegistry = new CommandRegistry();
        bukkitCommand = new BukkitCommandDataBuilder("test", integrationStub, TestCommandDevCommand.class).build();
    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integrationStub, null);
    }

    @Test
    void add() {

        Assertions.assertNull(commandRegistry.getValues(integrationStub));

        commandRegistry.add(integrationStub, bukkitCommand);

        Assertions.assertNotNull(commandRegistry.getValues(integrationStub));
        Assertions.assertEquals(1, commandRegistry.getValues(integrationStub).size());
        Assertions.assertEquals(bukkitCommand, commandRegistry.getValues(integrationStub).get(0));

    }

    @Test
    void remove() {

        commandRegistry.setValues(integrationStub, List.of(bukkitCommand));

        Assertions.assertNotNull(commandRegistry.getValues(integrationStub));
        Assertions.assertEquals(1, commandRegistry.getValues(integrationStub).size());
        Assertions.assertEquals(bukkitCommand, commandRegistry.getValues(integrationStub).get(0));

    }

}