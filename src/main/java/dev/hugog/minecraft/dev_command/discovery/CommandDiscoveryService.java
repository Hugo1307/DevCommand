package dev.hugog.minecraft.dev_command.discovery;

import dev.hugog.minecraft.dev_command.annotations.Arguments;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.builder.BukkitCommandDataBuilder;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.factories.ArgumentFactory;
import dev.hugog.minecraft.dev_command.integration.Integration;
import java.util.Arrays;

import dev.hugog.minecraft.dev_command.validation.AutoValidation;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CommandDiscoveryService {

    private final Integration integration;
    private final Reflections reflectionUtils;

    public CommandDiscoveryService(Integration integration) {
        this(integration, new Reflections(integration.getBasePackage()));
    }

    public CommandDiscoveryService(Integration integration, Reflections reflectionUtils) {
        this.integration = integration;
        this.reflectionUtils = reflectionUtils;
    }

    public Set<Class<? extends IDevCommand>> getCommandClasses() {
        return reflectionUtils.getTypesAnnotatedWith(Command.class)
                .stream()
                .filter(this::containsCommandAnnotation)
                .filter(this::isValidCommandClass)
                .map(this::getCommandClass)
                .collect(Collectors.toSet());
    }

    public List<AbstractCommandData> getDiscoveredCommandsData() {
        return getCommandClasses()
                .stream()
                .map(this::commandClassToCommandData)
                .collect(Collectors.toList());
    }

    public AbstractCommandData commandClassToCommandData(Class<? extends IDevCommand> commandExecutorClass) {

        CommandArgument[] arguments = null;
        Class<?>[] commandDependencies = null;
        AutoValidation autoValidation = null;

        if (containsArguments(commandExecutorClass)) {
            ArgumentFactory argumentFactory = new ArgumentFactory();
            Arguments executorArgumentsAnnotation = getArgumentsAnnotation(commandExecutorClass);
            arguments = Arrays.stream(executorArgumentsAnnotation.value())
                    .map(argumentFactory::generate)
                    .toArray(CommandArgument[]::new);
        }

        if (containsDependenciesAnnotation(commandExecutorClass)) {
            Dependencies dependenciesAnnotation = getDependenciesAnnotation(commandExecutorClass);
            commandDependencies = dependenciesAnnotation.dependencies();
        }

        if (containsAutoValidationAnnotation(commandExecutorClass)) {
            dev.hugog.minecraft.dev_command.annotations.AutoValidation autoValidationAnnotation = getAutoValidationAnnotation(commandExecutorClass);
            autoValidation = new AutoValidation(autoValidationAnnotation.permission(), autoValidationAnnotation.arguments(), autoValidationAnnotation.sender());
        }

        Command executorCommandAnnotation = getCommandAnnotation(commandExecutorClass);

        String commandAlias = executorCommandAnnotation.alias();
        String commandDescription = executorCommandAnnotation.description().isEmpty() ? null : executorCommandAnnotation.description();
        String commandPermission = executorCommandAnnotation.permission().isEmpty() ? null : executorCommandAnnotation.permission();
        boolean isPlayerOnly = executorCommandAnnotation.isPlayerOnly();

        return new BukkitCommandDataBuilder(commandAlias, integration, commandExecutorClass)
                .withDescription(commandDescription)
                .withPermission(commandPermission)
                .withPlayerOnly(isPlayerOnly)
                .withArguments(arguments)
                .withDependencies(commandDependencies)
                .withAutoValidation(autoValidation)
                .build();

    }

    public boolean containsCommandsWithRepeatedAliases() {

        List<AbstractCommandData> commandDataObjectsGenerated = getDiscoveredCommandsData();

        return getDiscoveredCommandsData().stream()
                .map(AbstractCommandData::getAlias)
                .distinct()
                .count() != commandDataObjectsGenerated.size();

    }

    private Command getCommandAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(Command.class);
    }

    private Arguments getArgumentsAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(Arguments.class);
    }

    private Dependencies getDependenciesAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(Dependencies.class);
    }

    private dev.hugog.minecraft.dev_command.annotations.AutoValidation getAutoValidationAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(dev.hugog.minecraft.dev_command.annotations.AutoValidation.class);
    }

    private boolean containsCommandAnnotation(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(Command.class);
    }

    private boolean containsArguments(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(Arguments.class);
    }

    private boolean containsDependenciesAnnotation(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(Dependencies.class);
    }

    private boolean containsAutoValidationAnnotation(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(dev.hugog.minecraft.dev_command.annotations.AutoValidation.class);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends IDevCommand> getCommandClass(Class<?> classToCheck) {
        return isValidCommandClass(classToCheck) ? (Class<? extends IDevCommand>) classToCheck : null;
    }

    private boolean isValidCommandClass(Class<?> classToCheck) {
        return IDevCommand.class.isAssignableFrom(classToCheck);
    }

}
