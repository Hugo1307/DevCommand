package me.hgsoft.minecraft.devcommand.register;

import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.utils.TestCommand;
import org.junit.jupiter.api.*;

import java.util.List;

class CommandRegistryTest {

    private Command commandStub;
    private CommandRegistry commandRegistry;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandRegistry = CommandRegistry.getInstance();
        commandStub = new Command("test", "Test command", TestCommand.class);
        integrationStub = new Integration("myIntegration");
    }

    @AfterEach
    void tearDown() {
        commandRegistry.setValues(integrationStub, null);
    }

    @Test
    void add() {

        Integration integration = new Integration("myIntegration");

        Assertions.assertNull(commandRegistry.getValues(integration));

        commandRegistry.add(integration, commandStub);

        Assertions.assertNotNull(commandRegistry.getValues(integration));
        Assertions.assertEquals(1, commandRegistry.getValues(integration).size());
        Assertions.assertEquals(commandStub, commandRegistry.getValues(integration).get(0));

    }

    @Test
    void remove() {

        Integration integration = new Integration("myIntegration");

        commandRegistry.setValues(integration, List.of(commandStub));

        Assertions.assertNotNull(commandRegistry.getValues(integration));
        Assertions.assertEquals(1, commandRegistry.getValues(integration).size());
        Assertions.assertEquals(commandStub, commandRegistry.getValues(integration).get(0));

    }

}