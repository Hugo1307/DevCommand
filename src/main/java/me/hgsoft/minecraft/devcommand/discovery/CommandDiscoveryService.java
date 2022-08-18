package me.hgsoft.minecraft.devcommand.discovery;

import lombok.Getter;
import me.hgsoft.minecraft.devcommand.annotations.ArgsValidation;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;
import me.hgsoft.minecraft.devcommand.integration.Integration;
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

    public Set<Class<? extends ICommandExecutor>> getCommandExecutorClasses() {
        return reflectionUtils.getTypesAnnotatedWith(Command.class)
                .stream()
                .filter(this::containsCommandAnnotation)
                .filter(this::isValidCommandClass)
                .map(this::getCommandExecutor)
                .collect(Collectors.toSet());
    }

    public AbstractCommand executorClassToCommand(Class<? extends ICommandExecutor> commandExecutorClass) {

        Class<?>[] argsValidationTypes = null;

        if (containsArgsValidator(commandExecutorClass)) {
            ArgsValidation executorArgsValidationAnnotation = getArgsValidationAnnotation(commandExecutorClass);
            argsValidationTypes = executorArgsValidationAnnotation.argsTypes();
        }

        Command executorCommandAnnotation = getCommandAnnotation(commandExecutorClass);

        String commandAlias = executorCommandAnnotation.alias();
        String commandDescription = executorCommandAnnotation.description().equals("") ? null : executorCommandAnnotation.description();
        String commandPermission = executorCommandAnnotation.permission().equals("") ? null : executorCommandAnnotation.permission();

        return new BukkitCommandBuilder(commandAlias, commandExecutorClass)
                .withDescription(commandDescription)
                .withPermission(commandPermission)
                .withArgumentTypes(argsValidationTypes)
                .build();

    }

    private Command getCommandAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(Command.class);
    }

    private ArgsValidation getArgsValidationAnnotation(Class<?> classToGetAnnotationFrom) {
        return classToGetAnnotationFrom.getAnnotation(ArgsValidation.class);
    }

    private boolean containsCommandAnnotation(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(Command.class);
    }

    private boolean containsArgsValidator(Class<?> classToCheck) {
        return classToCheck.isAnnotationPresent(ArgsValidation.class);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ICommandExecutor> getCommandExecutor(Class<?> classToCheck) {
        return isValidCommandClass(classToCheck) ? (Class<? extends ICommandExecutor>) classToCheck : null;
    }

    private boolean isValidCommandClass(Class<?> classToCheck) {
        return ICommandExecutor.class.isAssignableFrom(classToCheck);
    }

}
