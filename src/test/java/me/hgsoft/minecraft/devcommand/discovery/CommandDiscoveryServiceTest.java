package me.hgsoft.minecraft.devcommand.discovery;

import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.utils.test_classes.valid.ArgumentTestCommand;
import me.hgsoft.minecraft.devcommand.utils.test_classes.valid.NoAnnotationTestCommand;
import me.hgsoft.minecraft.devcommand.utils.test_classes.valid.TestCommand;
import me.hgsoft.minecraft.devcommand.utils.test_classes.invalid.TestCommandCopy;
import me.hgsoft.minecraft.devcommand.validators.IntegerArgument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CommandDiscoveryServiceTest {

    @Mock
    private Reflections reflectionsUtilsMock;

    @Mock
    private Integration integrationMock;

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
                                .extracting(BukkitCommandData::getMandatoryArguments)
                                .extracting(classes ->
                                        assertThat(classes).containsExactlyInAnyOrder(IntegerArgument.class)
                                ).isNotNull()
                );

        assertThat(commandDiscoveryService.getDiscoveredCommandsData())
                .first()
                .isInstanceOfSatisfying(BukkitCommandData.class, bukkitCommandData ->
                        assertThat(bukkitCommandData).extracting(BukkitCommandData::getOptionalArguments).isNull()
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
                                .extracting(BukkitCommandData::getMandatoryArguments)
                                .extracting(classes ->
                                        assertThat(classes).containsExactlyInAnyOrder(IntegerArgument.class)
                                ).isNotNull()
                );

        assertThat(commandDiscoveryService.commandClassToCommandData(TestCommand.class))
                .isInstanceOfSatisfying(BukkitCommandData.class, bukkitCommandData ->
                        assertThat(bukkitCommandData).extracting(BukkitCommandData::getOptionalArguments).isNull()
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