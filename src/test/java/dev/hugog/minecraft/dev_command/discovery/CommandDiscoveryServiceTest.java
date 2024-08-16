package dev.hugog.minecraft.dev_command.discovery;

import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.arguments.validators.StringArgumentValidator;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.utils.test_classes.invalid.TestCommandCopy;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.ArgumentTestCommand;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.NoAnnotationTestCommand;
import dev.hugog.minecraft.dev_command.utils.test_classes.valid.TestCommand;
import dev.hugog.minecraft.dev_command.arguments.validators.IntegerArgumentValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandDiscoveryServiceTest {

    @Mock
    private Reflections reflectionsUtilsMock;

    @InjectMocks
    private CommandDiscoveryService commandDiscoveryService;

    @Test
    @DisplayName("Test method to get classes annotated with @Command")
    void getCommandClasses() {

        when(reflectionsUtilsMock.getTypesAnnotatedWith(Command.class)).thenReturn(Set.of(TestCommand.class, NoAnnotationTestCommand.class));

        // Contains Only TestCommand cause the other one does not contain the @Command annotation.
        assertThat(commandDiscoveryService.getCommandClasses())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .containsOnly(TestCommand.class);

    }

    @Test
    @DisplayName("Test method to generate command data for all the detected commands.")
    void getDiscoveredCommandsData() {

        when(reflectionsUtilsMock.getTypesAnnotatedWith(Command.class)).thenReturn(Set.of(TestCommand.class));

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        // Below:
        // Check if the values from @Command annotation in TestCommand class were successfully introduced
        // in the command data class generated.

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .extracting(AbstractCommandData::getName)
                .containsOnlyNulls();

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .extracting(AbstractCommandData::getAlias)
                .containsExactly("test");

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .extracting(AbstractCommandData::getDescription)
                .containsExactly("Bukkit Test Command!");

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .extracting(AbstractCommandData::getExecutor)
                .first()
                .isEqualTo(TestCommand.class);

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .first()
                .isInstanceOf(BukkitCommandData.class);

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .first()
                .isInstanceOfSatisfying(BukkitCommandData.class, bukkitCommandData ->
                        assertThat(bukkitCommandData).extracting(BukkitCommandData::getPermission).isEqualTo("command.bukkit_test")
                );

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .first()
                .isInstanceOfSatisfying(BukkitCommandData.class, bukkitCommandData ->
                        assertThat(bukkitCommandData)
                                .extracting(BukkitCommandData::getArguments)
                                .extracting(arguments -> assertThat(arguments)
                                    .containsExactlyInAnyOrder(
                                        new CommandArgument("string", "String to test", 0, StringArgumentValidator.class, false),
                                        new CommandArgument("number", "Number to test", 1, IntegerArgumentValidator.class, true)
                                    )
                                ).isNotNull()
                );

    }

    @Test
    @DisplayName("Test method to convert from command class to command data object.")
    void commandClassToCommandData() {

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .isNotNull()
                .isInstanceOf(BukkitCommandData.class);

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .extracting(AbstractCommandData::getName)
                .isNull();

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .extracting(AbstractCommandData::getAlias)
                .isEqualTo("test");

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .extracting(AbstractCommandData::getDescription)
                .isEqualTo("Bukkit Test Command!");

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .extracting(AbstractCommandData::getExecutor)
                .isEqualTo(TestCommand.class);

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .isInstanceOfSatisfying(BukkitCommandData.class, bukkitCommandData ->
                        assertThat(bukkitCommandData).extracting(BukkitCommandData::getPermission).isEqualTo("command.bukkit_test")
                );

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
            .isInstanceOfSatisfying(BukkitCommandData.class, bukkitCommandData ->
                assertThat(bukkitCommandData)
                    .extracting(BukkitCommandData::getArguments)
                    .extracting(arguments -> assertThat(arguments)
                        .containsExactlyInAnyOrder(
                            new CommandArgument("string", "String to test", 0, StringArgumentValidator.class, false),
                            new CommandArgument("number", "Number to test", 1, IntegerArgumentValidator.class, true)
                        )
                    ).isNotNull()
            );

    }

    @Test
    @DisplayName("Test if commands with repeated aliases are detected successfully.")
    void containsCommandsWithRepeatedAliases() {

        // Both TestCommand and TestCommandCopy have the same alias
        when(reflectionsUtilsMock.getTypesAnnotatedWith(Command.class))
                .thenReturn(Set.of(TestCommand.class, TestCommandCopy.class));

        assertThat(commandDiscoveryService.containsCommandsWithRepeatedAliases()).isTrue();

        // TestCommand and ArgumentTestCommand have different aliases
        when(reflectionsUtilsMock.getTypesAnnotatedWith(Command.class))
                .thenReturn(Set.of(TestCommand.class, ArgumentTestCommand.class));

        assertThat(commandDiscoveryService.containsCommandsWithRepeatedAliases()).isFalse();

    }

}