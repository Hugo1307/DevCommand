package dev.hugog.minecraft.dev_command.commands;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.exceptions.ArgumentsConfigException;
import dev.hugog.minecraft.dev_command.exceptions.InvalidArgumentsException;
import dev.hugog.minecraft.dev_command.exceptions.PermissionConfigException;
import dev.hugog.minecraft.dev_command.arguments.parsers.BooleanArgumentParser;
import dev.hugog.minecraft.dev_command.arguments.parsers.DoubleArgumentParser;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BukkitDevCommandTest {

    @Mock
    private CommandSender commandSenderMock;
    @Mock
    private BukkitCommandData bukkitCommandDataMock;

    private BukkitDevCommand bukkitDevCommandStub;

    @BeforeEach
    void setUp() {
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, null) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };
    }

    @Test
    @DisplayName("Test the hasPermissionToExecuteCommand method when player HAS permission.")
    void hasPermissionToExecuteCommand() {

        String commandPermission = "commands.no_annotation_test";

        when(bukkitCommandDataMock.getPermission()).thenReturn(commandPermission);
        when(commandSenderMock.hasPermission(commandPermission)).thenReturn(Boolean.TRUE);

        assertThat(bukkitDevCommandStub.hasPermissionToExecuteCommand()).isTrue();

        verify(bukkitCommandDataMock, times(2)).getPermission();
        verify(commandSenderMock, times(1)).hasPermission(commandPermission);

    }

    @Test
    @DisplayName("Test the hasPermissionToExecuteCommand method when player DOES NOT HAVE permission.")
    void hasPermissionToExecuteCommand_noPermission() {

        String commandPermission = "commands.no_annotation_test";

        when(bukkitCommandDataMock.getPermission()).thenReturn(commandPermission);
        when(commandSenderMock.hasPermission(commandPermission)).thenReturn(Boolean.FALSE);

        assertThat(bukkitDevCommandStub.hasPermissionToExecuteCommand()).isFalse();

        verify(bukkitCommandDataMock, times(2)).getPermission();
        verify(commandSenderMock, times(1)).hasPermission(commandPermission);

    }

    @Test
    @DisplayName("Test the behaviour when the method is called when the permission is null a.k.a. not defined.")
    void hasPermissionToExecuteCommand_withUndefinedPermission() {

        when(bukkitCommandDataMock.getPermission()).thenReturn(null);

        assertThrows(PermissionConfigException.class, () -> bukkitDevCommandStub.hasPermissionToExecuteCommand(),
                String.format("Unable to find the permission for the command %s. Have you configured any permission at all?", bukkitCommandDataMock.getName()));

        verify(bukkitCommandDataMock, times(1)).getPermission();
        verify(commandSenderMock, never()).hasPermission(anyString());

    }

    @Test
    @DisplayName("Test the canExecuteCommand method when the command is player only and the sender is a player.")
    void canExecuteCommand() {

        Player playerMock = mock(Player.class);
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, playerMock, null) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.isPlayerOnly()).thenReturn(true);
        assertThat(bukkitDevCommandStub.canSenderExecuteCommand()).isTrue();

    }

    @Test
    @DisplayName("Test the canExecuteCommand method when the command is not player only and the sender is not a player.")
    void canExecuteCommand_withoutPlayerOnlyFlag() {

        when(bukkitCommandDataMock.isPlayerOnly()).thenReturn(false);
        assertThat(bukkitDevCommandStub.canSenderExecuteCommand()).isTrue();

    }

    @Test
    @DisplayName("Test the canExecuteCommand method when the command player only and the sender is not a player.")
    void canExecuteCommand__withPlayerOnlyFlag() {

        when(bukkitCommandDataMock.isPlayerOnly()).thenReturn(true);
        assertThat(bukkitDevCommandStub.canSenderExecuteCommand()).isFalse();

    }

    @Test
    @DisplayName("Test command mandatory arguments validation.")
    void hasValidArgs_Mandatory() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1", "hey"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("number", "Number to test", 0, IntegerArgumentParser.class, false),
            new CommandArgument("string", "String to test", 1, StringArgumentParser.class, false)}
        );

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test invalid mandatory arguments validation.")
    void hasValidArgs_Mandatory_InvalidArguments() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1", "notADouble"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("number", "Number to test", 0, IntegerArgumentParser.class, false),
            new CommandArgument("double", "Double to test", 1, DoubleArgumentParser.class, false)}
        );

        // Fails cause second argument is not a double.
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("double", "Double to test", 0, DoubleArgumentParser.class, false),
            new CommandArgument("string", "String to test", 1, StringArgumentParser.class, false)}
        );

        // Fails cause the first argument is an Integer, not a Double.
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

    }

    @Test
    @DisplayName("Test validation for optional arguments.")
    void hasValidArgs_Optional() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"Some String"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments())
            .thenReturn(new CommandArgument[]{
                new CommandArgument("string", "String to test", 0, StringArgumentParser.class, true)}
            );

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test invalid optional arguments validation.")
    void hasValidArgs_Optional_InvalidArguments() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"notAInteger"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments())
            .thenReturn(new CommandArgument[]{
                new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, true)}
            );

        // Fails cause the optional argument (first argument) is not an Integer - is a String
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

        when(bukkitCommandDataMock.getArguments())
            .thenReturn(new CommandArgument[]{
                new CommandArgument("boolean", "Boolean to test", 0, BooleanArgumentParser.class, true)}
            );

        // Fails cause the optional argument (first argument) is not a Boolean - is a String
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

    }

    @Test
    @DisplayName("Test mandatory AND optional arguments validation.")
    void hasValidArgs_Mandatory_And_Optional() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1","mandatoryArgument","1.2"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments())
            .thenReturn(new CommandArgument[]{
                new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, false),
                new CommandArgument("string", "String to test", 1, StringArgumentParser.class, false),
                new CommandArgument("double", "Double to test", 2, DoubleArgumentParser.class, true)}
            );

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test invalid mandatory AND optional arguments validation.")
    void hasValidArgs_Mandatory_And_Optional_Invalid() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1","true","1.2"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments())
            .thenReturn(new CommandArgument[]{
                new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, false),
                new CommandArgument("boolean", "Boolean to test", 1, BooleanArgumentParser.class, false),
                new CommandArgument("boolean", "Boolean to test", 2, BooleanArgumentParser.class, true)}
            );

        // Fails cause although the mandatory arguments are valid the optional arguments are not valid.
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

        when(bukkitCommandDataMock.getArguments())
            .thenReturn(new CommandArgument[]{
                new CommandArgument("string", "String to test", 0, StringArgumentParser.class, false),
                new CommandArgument("integer", "Integer to test", 1, IntegerArgumentParser.class, false),
                new CommandArgument("double", "Double to test", 2, DoubleArgumentParser.class, true)}
            );

        // Fails cause although the optional arguments are valid the mandatory arguments are not valid
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

    }

    @Test
    @DisplayName("Test argument validation for unconventional arguments.")
    void hasValidArgs_WeirdArguments() {

        String[] arguments = new String[] {"-78","yes",".22", "-0.44"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, false),
            new CommandArgument("boolean", "Boolean to test", 1, BooleanArgumentParser.class, false),
            new CommandArgument("double", "Double to test", 2, DoubleArgumentParser.class, false),
            new CommandArgument("double", "Double to test", 3, DoubleArgumentParser.class, false)}
        );

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test argument validation without optional args.")
    void hasValidArgs_OptionalArgs() {

        String[] arguments = new String[] {"-78","yes"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, false),
            new CommandArgument("boolean", "Boolean to test", 1, BooleanArgumentParser.class, false),
            new CommandArgument("double", "Double to test", 2, DoubleArgumentParser.class, true)}
        );

        // The argument validation passes cause the argument missing is optional
        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test getArgumentParser() method with valid arguments.")
    void parseArgument() {

        String[] arguments = new String[] {"-78","yes",".22", "-0.44"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, false)}
        );

        assertDoesNotThrow(() -> bukkitDevCommandStub.getArgumentParser(0));
        assertThat(bukkitDevCommandStub.getArgumentParser(0))
                .isNotNull();

    }

    @Test
    @DisplayName("Test getArgumentParser() method with invalid arguments.")
    void parseArgument_Invalid() {

        String[] arguments = new String[] {"-78","yes",".22", "-0.44"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(new CommandArgument[]{
            new CommandArgument("integer", "Integer to test", 0, IntegerArgumentParser.class, false),
            new CommandArgument("boolean", "Boolean to test", 1, BooleanArgumentParser.class, false),
            new CommandArgument("integer", "Integer to test", 2, IntegerArgumentParser.class, false)}
        );

        assertFalse(bukkitDevCommandStub.hasValidArgs());
        assertThrows(InvalidArgumentsException.class, () -> bukkitDevCommandStub.getArgumentParser(2));

    }

    @Test
    @DisplayName("Test for call of argument validation without configuration.")
    void argumentsConfigExceptionTest() {

        String[] arguments = new String[] {"-78","yes",".22", "-0.44"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
            @Override
            public List<String> onTabComplete(String[] args) {
                return List.of();
            }
        };

        when(bukkitCommandDataMock.getArguments()).thenReturn(null);

        assertThrows(ArgumentsConfigException.class,
                () -> bukkitDevCommandStub.hasValidArgs(),
                String.format("Unable to find arguments configuration for the command %s. Have you configured any arguments for the command?", bukkitCommandDataMock.getName()));

    }

}