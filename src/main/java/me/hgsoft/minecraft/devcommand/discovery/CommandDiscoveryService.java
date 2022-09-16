package me.hgsoft.minecraft.devcommand.discovery;

import lombok.Getter;
import me.hgsoft.minecraft.devcommand.annotations.ArgsValidation;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.annotations.Dependencies;
import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandDataBuilder;
import me.hgsoft.minecraft.devcommand.commands.IDevCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.validators.CommandArgument;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CommandDiscoveryService {

    private final Integration integration;
    private final Reflections reflectionUtils;

    public CommandDiscoveryService(Integration integration) {
        this.integration = integration;
        this.reflectionUtils = new Reflections(integration.getBasePackage());
    }

    public Set<Class<? extends IDevCommand>> getCommandExecutorClasses() {
        return reflectionUtils.getTypesAnnotatedWith(Command.class)
                .stream()
                .filter(this::containsCommandAnnotation)
                .filter(this::isValidCommandClass)
                .map(this::getCommandExecutor)
                .collect(Collectors.toSet());
    }

    public AbstractCommandData executorClassToCommand(Class<? extends IDevCommand> commandExecutorClass) {

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
        String commandDescription = executorCommandAnnotation.description().equals("") ? null : executorCommandAnnotation.description();
        String commandPermission = executorCommandAnnotation.permission().equals("") ? null : executorCommandAnnotation.permission();

        return new BukkitCommandDataBuilder(commandAlias, integration, commandExecutorClass)
                .withDescription(commandDescription)
                .withPermission(commandPermission)
                .withMandatoryArguments(argsValidationTypes)
                .withDependencies(commandDependencies)
                .build();

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
    private Class<? extends IDevCommand> getCommandExecutor(Class<?> classToCheck) {
        return isValidCommandClass(classToCheck) ? (Class<? extends IDevCommand>) classToCheck : null;
    }

    private boolean isValidCommandClass(Class<?> classToCheck) {
        return IDevCommand.class.isAssignableFrom(classToCheck);
    }

}
