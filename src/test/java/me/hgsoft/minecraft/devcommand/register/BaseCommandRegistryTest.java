package me.hgsoft.minecraft.devcommand.register;

import me.hgsoft.minecraft.devcommand.commands.BaseCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.utils.TestBaseCommand;
import org.junit.jupiter.api.*;

import java.util.List;

class BaseCommandRegistryTest {

    private BaseCommand baseCommandStub;
    private CommandRegistry commandRegistry;
    private Integration integrationStub;

    @BeforeEach
    void setUp() {
        commandRegistry = CommandRegistry.getInstance();
        baseCommandStub = new BaseCommand("test", "Test command", TestBaseCommand.class);
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

        commandRegistry.add(integration, baseCommandStub);

        Assertions.assertNotNull(commandRegistry.getValues(integration));
        Assertions.assertEquals(1, commandRegistry.getValues(integration).size());
        Assertions.assertEquals(baseCommandStub, commandRegistry.getValues(integration).get(0));

    }

    @Test
    void remove() {

        Integration integration = new Integration("myIntegration");

        commandRegistry.setValues(integration, List.of(baseCommandStub));

        Assertions.assertNotNull(commandRegistry.getValues(integration));
        Assertions.assertEquals(1, commandRegistry.getValues(integration).size());
        Assertions.assertEquals(baseCommandStub, commandRegistry.getValues(integration).get(0));

    }

}