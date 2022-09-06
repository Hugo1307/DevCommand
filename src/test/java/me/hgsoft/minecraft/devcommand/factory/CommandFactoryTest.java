package me.hgsoft.minecraft.devcommand.factory;

import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.commands.executors.IDevCommandExecutor;
import me.hgsoft.minecraft.devcommand.factories.CommandFactory;
import me.hgsoft.minecraft.devcommand.factories.IObjectFactory;
import me.hgsoft.minecraft.devcommand.utils.BukkitTestCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    private IObjectFactory<IDevCommandExecutor, AbstractCommandData> bukkitCommandFactory;
    private BukkitCommandData bukkitCommand;

    @BeforeEach
    void setUp() {
        bukkitCommandFactory = new CommandFactory(null, "good", "afternoon");
        bukkitCommand = new BukkitCommandBuilder("test", BukkitTestCommand.class)
                .withName("Test Command")
                .withDescription("Bukkit Test Command")
                .build();
    }

    @Test
    void generateExecutorForBukkitCommand() {
        IDevCommandExecutor generatedBukkitCommandExecutor = bukkitCommandFactory.generate(bukkitCommand);
        Assertions.assertTrue(generatedBukkitCommandExecutor instanceof BukkitTestCommand);
    }

}