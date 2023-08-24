package me.hgsoft.minecraft.devcommand.executors;

import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.exceptions.ArgumentsConfigException;
import me.hgsoft.minecraft.devcommand.exceptions.PermissionConfigException;
import me.hgsoft.minecraft.devcommand.validators.BooleanArgument;
import me.hgsoft.minecraft.devcommand.validators.DoubleArgument;
import me.hgsoft.minecraft.devcommand.validators.IntegerArgument;
import me.hgsoft.minecraft.devcommand.validators.StringArgument;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("Test command mandatory arguments validation.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_Mandatory() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1", "hey"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[]{IntegerArgument.class, StringArgument.class});

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test invalid mandatory arguments validation.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_Mandatory_InvalidArguments() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1", "notADouble"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[]{IntegerArgument.class, DoubleArgument.class});

        // Fails cause second argument is not a double.
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[]{DoubleArgument.class, StringArgument.class});

        // Fails cause the first argument is an Integer, not a Double.
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

    }

    @Test
    @DisplayName("Test validation for optional arguments.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_Optional() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"Some String"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[0]);
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[]{StringArgument.class});

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test invalid optional arguments validation.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_Optional_InvalidArguments() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"notAInteger"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[0]);
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[] {IntegerArgument.class});

        // Fails cause the optional argument (first argument) is not an Integer - is a String
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[0]);
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[] {BooleanArgument.class});

        // Fails cause the optional argument (first argument) is not a Boolean - is a String
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

    }

    @Test
    @DisplayName("Test mandatory AND optional arguments validation.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_Mandatory_And_Optional() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1","mandatoryArgument","1.2"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[] {IntegerArgument.class, StringArgument.class});
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[] {DoubleArgument.class});

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test invalid mandatory AND optional arguments validation.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_Mandatory_And_Optional_Invalid() {

        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, new String[] {"1","true","1.2"}) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[] {IntegerArgument.class, BooleanArgument.class});
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[] {BooleanArgument.class});

        // Fails cause although the mandatory arguments are valid the optional arguments are not valid.
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[] {DoubleArgument.class, IntegerArgument.class});
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[] {BooleanArgument.class});

        // Fails cause although the optional arguments are valid the mandatory arguments are not valid
        assertThat(bukkitDevCommandStub.hasValidArgs()).isFalse();

    }

    @Test
    @DisplayName("Test argument validation for unconventional arguments.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_WeirdArguments() {

        String[] arguments = new String[] {"-78","yes",".22", "-0.44"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[] {IntegerArgument.class, BooleanArgument.class, DoubleArgument.class, DoubleArgument.class});

        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

    }

    @Test
    @DisplayName("Test argument validation without optional args.")
    @SuppressWarnings("unchecked")
    void hasValidArgs_OptionalArgs() {

        String[] arguments = new String[] {"-78","yes"};
        bukkitDevCommandStub = new BukkitDevCommand(bukkitCommandDataMock, commandSenderMock, arguments) {
            @Override
            public void execute() {
                System.out.println("Command Executed");
            }
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(new Class[] {IntegerArgument.class, BooleanArgument.class});
        // Request validation for optional argument - not present in this case
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(new Class[] {StringArgument.class});

        // The argument validation passes cause the argument missing is optional
        assertThat(bukkitDevCommandStub.hasValidArgs()).isTrue();

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
        };

        when(bukkitCommandDataMock.getMandatoryArguments()).thenReturn(null);
        when(bukkitCommandDataMock.getOptionalArguments()).thenReturn(null);

        assertThrows(ArgumentsConfigException.class,
                () -> bukkitDevCommandStub.hasValidArgs(),
                String.format("Unable to find arguments configuration for the command %s. Have you added configured any arguments for the command?", bukkitCommandDataMock.getName()));

    }

}