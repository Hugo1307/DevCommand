package me.hgsoft.minecraft.devcommand.register;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import org.junit.jupiter.api.*;

import java.util.List;

class CommandRegistryTest {

    private BukkitCommandData bukkitCommand;
    private CommandRegistry commandRegistry;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandRegistry = CommandRegistry.getInstance();
        bukkitCommand = new BukkitCommandBuilder("test", BukkitTestCommand.class).build();
        integrationStub = new Integration("myIntegration", null);
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