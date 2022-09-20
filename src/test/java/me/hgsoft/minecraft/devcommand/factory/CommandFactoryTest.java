package me.hgsoft.minecraft.devcommand.factory;

import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.commands.IDevCommand;
import me.hgsoft.minecraft.devcommand.factories.CommandFactory;
import me.hgsoft.minecraft.devcommand.factories.IObjectFactory;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.utils.test_classes.valid.TestCommand;
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
        bukkitCommandFactory = new CommandFactory(null, "good", "afternoon");
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