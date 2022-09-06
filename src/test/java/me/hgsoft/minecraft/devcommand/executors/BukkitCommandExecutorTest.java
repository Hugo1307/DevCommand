package me.hgsoft.minecraft.devcommand.executors;

import me.hgsoft.minecraft.devcommand.CommandHandler;
import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.exceptions.ArgumentsConfigException;
import me.hgsoft.minecraft.devcommand.exceptions.PermissionConfigException;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;
import me.hgsoft.minecraft.devcommand.utils.arguments.ArgumentTestCommand;
import me.hgsoft.minecraft.devcommand.utils.arguments.NoAnnotationArgumentTestCommand;
import me.hgsoft.minecraft.devcommand.factories.validators.BooleanArgument;
import me.hgsoft.minecraft.devcommand.factories.validators.DoubleArgument;
import me.hgsoft.minecraft.devcommand.factories.validators.IntegerArgument;
import me.hgsoft.minecraft.devcommand.factories.validators.StringArgument;
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
class BukkitCommandExecutorTest {

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
        NoAnnotationArgumentTestCommand.called = false;
        NoAnnotationArgumentTestCommand.hasPermission = false;
        NoAnnotationArgumentTestCommand.hasValidArgs = false;
        commandRegistry.setValues(integration, null);
    }

    @Test
    void hasPermissionToExecuteCommand() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .build();

        when(commandSender.hasPermission(bukkitCommand.getPermission())).thenReturn(true);

        commandHandler.registerCommand(integration, bukkitCommand);

        try {
            commandHandler.executeCommandByAlias(integration, "test", commandSender);
        } catch (ArgumentsConfigException e) {
            // Ignored exception
        }

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertTrue(NoAnnotationArgumentTestCommand.hasPermission);

    }

    @Test
    void hasPermissionToExecuteCommand_NotConfiguredException() {

        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);

        assertThrows(PermissionConfigException.class,
                () -> commandHandler.executeCommandByAlias(integration, "test", commandSender),
                String.format("Unable to find the permission for the command %s. Have you configured any permission at all?", bukkitCommand.getName()));

    }

    @Test
    void hasValidArgs_Mandatory() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(IntegerArgument.class, DoubleArgument.class, StringArgument.class, BooleanArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "1", "22.222", "hey", "true");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertTrue(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_InvalidArguments() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(IntegerArgument.class, DoubleArgument.class, StringArgument.class, BooleanArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "Hello!", ".22aa", "Valid!", "ash");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertFalse(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Optional() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "1");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertTrue(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Optional_InvalidArguments() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "Hey", ".22");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertFalse(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(BooleanArgument.class)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "True", "22");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertTrue(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional_Invalid() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(BooleanArgument.class)
                .withOptionalArguments(IntegerArgument.class, DoubleArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "true", "Boa tarde", ".22");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertFalse(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_WeirdArguments() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .withMandatoryArguments(IntegerArgument.class, DoubleArgument.class, StringArgument.class, BooleanArgument.class)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);
        commandHandler.executeCommandByAlias(integration, "test", commandSender, "-78", ".222", "20", "yes");

        assertTrue(NoAnnotationArgumentTestCommand.called);
        assertTrue(NoAnnotationArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional_Annotation() {

        commandHandler.initCommandsAutoConfiguration(integration);
        commandHandler.executeCommandByAlias(integration, "test_arg", commandSender, "22");

        assertTrue(ArgumentTestCommand.called);
        assertTrue(ArgumentTestCommand.hasValidArgs);

    }

    @Test
    void hasValidArgs_Mandatory_And_Optional_Annotation_Invalid() {

        commandHandler.initCommandsAutoConfiguration(integration);
        commandHandler.executeCommandByAlias(integration, "test_arg", commandSender, "true");

        assertTrue(ArgumentTestCommand.called);
        assertFalse(ArgumentTestCommand.hasValidArgs);

    }

    @Test
    void argumentsConfigExceptionTest() {

        String commandPermission = "commands.no_annotation_test";
        BukkitCommand bukkitCommand = new BukkitCommandBuilder("test", NoAnnotationArgumentTestCommand.class)
                .withPermission(commandPermission)
                .build();

        commandHandler.registerCommand(integration, bukkitCommand);

        assertThrows(ArgumentsConfigException.class,
                () -> commandHandler.executeCommandByAlias(integration, "test", commandSender),
                String.format("Unable to find arguments configuration for the command %s. Have you added configured any arguments for the command?", bukkitCommand.getName()));

    }

}