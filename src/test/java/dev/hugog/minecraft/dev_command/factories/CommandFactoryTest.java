package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.builder.BukkitCommandDataBuilder;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
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
        bukkitCommandFactory = new CommandFactory(new String[] {"test", "good", "afternoon"}, (Object) null);
        bukkitCommand = new BukkitCommandDataBuilder("test", integrationMock, TestCommand.class)
                .withName("Test Command")
                .withDescription("Bukkit Test Command")
                .build();
    }

    @Test
    void generateExecutorForBukkitCommand() {
        IDevCommand generatedBukkitCommandExecutor = bukkitCommandFactory.generate(bukkitCommand);
        Assertions.assertInstanceOf(TestCommand.class, generatedBukkitCommandExecutor);
    }

}