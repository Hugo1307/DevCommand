package dev.hugog.minecraft.dev_command.factory;

import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.builder.BukkitCommandDataBuilder;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.factories.CommandFactory;
import dev.hugog.minecraft.dev_command.factories.IObjectFactory;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.TestCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    private IObjectFactory<IDevCommand, AbstractCommandData> bukkitCommandFactory;
    private BukkitCommandData bukkitCommand;

    @Mock
    private Integration integrationMock;

    @BeforeEach
    void setUp() {
        bukkitCommandFactory = new CommandFactory(null, new String[] {"good", "afternoon"});
        bukkitCommand = new BukkitCommandDataBuilder("test", integrationMock, TestCommand.class)
                .withName("Test Command")
                .withDescription("Bukkit Test Command")
                .build();
    }

    @Test
    void generateExecutorForBukkitCommand() {
        IDevCommand generatedBukkitCommandExecutor = bukkitCommandFactory.generate(bukkitCommand);
        Assertions.assertTrue(generatedBukkitCommandExecutor instanceof TestCommand);
    }

}