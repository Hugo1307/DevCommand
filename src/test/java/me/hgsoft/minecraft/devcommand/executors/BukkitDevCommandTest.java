package me.hgsoft.minecraft.devcommand.executors;

import me.hgsoft.minecraft.devcommand.CommandHandler;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.exceptions.ArgumentsConfigException;
import me.hgsoft.minecraft.devcommand.exceptions.PermissionConfigException;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.arguments.ArgumentDevCommandTestCommand;
import me.hgsoft.minecraft.devcommand.utils.arguments.NoAnnotationArgumentTestCommandDevCommand;
import me.hgsoft.minecraft.devcommand.validators.BooleanArgument;
import me.hgsoft.minecraft.devcommand.validators.DoubleArgument;
import me.hgsoft.minecraft.devcommand.validators.IntegerArgument;
import me.hgsoft.minecraft.devcommand.validators.StringArgument;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BukkitDevCommandTest {

    @Mock
    private CommandSender commandSender;

    private Integration integration;
    private CommandHandler commandHandler;
    private CommandRegistry commandRegistry;

    @BeforeEach
    void setUp() {
        integration = new Integration("MyPlugin", "me");
        commandHandler = CommandHandler.createOrGetInstance();
        commandRegistry = CommandRegistry.getInstance();
    }

    @AfterEach
    void tearDown() {
        NoAnnotationArgumentTestCommandDevCommand.called = false;
        NoAnnotationArgumentTestCommandDevCommand.hasPermission = false;
        NoAnnotationArgumentTestCommandDevCommand.hasValidArgs = false;
        commandRegistry.setValues(integration, null);
    }

    @Test
    void hasPermissionToExecuteCommand() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .build();

        when(commandSender.hasPermission(bukkitCommand.getPermission())).thenReturn(true);

        commandHandler.registerCommand(integration, bukkitCommand);

        try {
            commandHandler.executeCommandByAlias(integration, "test", commandSender);
        } catch (ArgumentsConfigException e) {
            // Ignored exception
        }

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertTrue(NoAnnotationArgumentTestCommandDevCommand.hasPermission);

    }

    @Test
    void hasPermissionToExecuteCommand_NotConfiguredException() {

        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);

        assertThrows(PermissionConfigException.class,
                () -> commandHandler.executeCommandByAlias(integration, "test", commandSender),
                String.format("Unable to find the permission for the command %s. Have you configured any permission at all?", bukkitCommand.getName()));

    }

    @Test
    void hasValidArgs_Mandatory() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(IntegerArgument.class, DoubleArgument.class, StringArgument.class, BooleanArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "1", "22.222", "hey", "true");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertTrue(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_InvalidArguments() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(IntegerArgument.class, DoubleArgument.class, StringArgument.class, BooleanArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "Hello!", ".22aa", "Valid!", "ash");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertFalse(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Optional() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "1");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertTrue(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Optional_InvalidArguments() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "Hey", ".22");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertFalse(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(BooleanArgument.class)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "True", "22");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertTrue(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional_Invalid() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(BooleanArgument.class)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "true", "Boa tarde", ".22");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertFalse(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_WeirdArguments() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(IntegerArgument.class, DoubleArgument.class, StringArgument.class, BooleanArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "-78", ".222", "20", "yes");

        assertTrue(NoAnnotationArgumentTestCommandDevCommand.called);
        assertTrue(NoAnnotationArgumentTestCommandDevCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional_Annotation() {

        commandHandler.initCommandsAutoConfiguration(integration);
        commandHandler.executeCommandByAlias(integration, "test_arg", commandSender, "22");

        assertTrue(ArgumentDevCommandTestCommand.called);
        assertTrue(ArgumentDevCommandTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional_Annotation_Invalid() {

        commandHandler.initCommandsAutoConfiguration(integration);
        commandHandler.executeCommandByAlias(integration, "test_arg", commandSender, "true");

        assertTrue(ArgumentDevCommandTestCommand.called);
        assertFalse(ArgumentDevCommandTestCommand.hasValidArgs);

    }

    @Test
    void argumentsConfigExceptionTest() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommandData bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommandDevCommand.class)
                .withPermission(commandPermission)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);

        assertThrows(ArgumentsConfigException.class,
                () -> commandHandler.executeCommandByAlias(integration, "test", commandSender),
                String.format("Unable to find arguments configuration for the command %s. Have you added configured any arguments for the command?", bukkitCommand.getName()));

    }

}