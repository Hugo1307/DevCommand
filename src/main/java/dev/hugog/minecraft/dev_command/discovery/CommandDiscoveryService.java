package dev.hugog.minecraft.dev_command.discovery;

import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.builder.BukkitCommandDataBuilder;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.validators.CommandArgument;
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

        Class<? extends CommandArgument<?>>[] argsValidationTypes = null;
        Class<?>[] commandDependencies = null;

        if (containsArgsValidator(commandExecutorClass)) {
            ArgsValidation executorArgsValidationAnnotation = getArgsValidationAnnotation(commandExecutorClass);
            argsValidationTypes = executorArgsValidationAnnotation.argsTypes();
        }

        if (containsDependenciesAnnotation(commandExecutorClass)) {
            Dependencies dependenciesAnnotation = getDependenciesAnnotation(commandExecutorClass);
            commandDependencies = dependenciesAnnotation.dependencies();
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
                .withMandatoryArguments(argsValidationTypes)
                .withDependencies(commandDependencies)
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

    private ArgsValidation getArgsValidationAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(ArgsValidation.class);
    }

    private Dependencies getDependenciesAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(Dependencies.class);
    }

    private boolean containsCommandAnnotation(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(Command.class);
    }

    private boolean containsArgsValidator(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(ArgsValidation.class);
    }

    private boolean containsDependenciesAnnotation(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(Dependencies.class);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends IDevCommand> getCommandClass(Class<?> classToCheck) {
        return isValidCommandClass(classToCheck) ? (Class<? extends IDevCommand>) classToCheck : null;
    }

    private boolean isValidCommandClass(Class<?> classToCheck) {
        return IDevCommand.class.isAssignableFrom(classToCheck);
    }

}
