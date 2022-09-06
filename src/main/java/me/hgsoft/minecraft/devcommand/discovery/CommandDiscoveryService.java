package me.hgsoft.minecraft.devcommand.discovery;

import lombok.Getter;
import me.hgsoft.minecraft.devcommand.annotations.ArgsValidation;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.builder.BukkitCommandBuilder;
import me.hgsoft.minecraft.devcommand.commands.executors.IDevCommandExecutor;
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

    public Set<Class<? extends IDevCommandExecutor>> getCommandExecutorClasses() {
        return reflectionUtils.getTypesAnnotatedWith(Command.class)
                .stream()
                .filter(this::containsCommandAnnotation)
                .filter(this::isValidCommandClass)
                .map(this::getCommandExecutor)
                .collect(Collectors.toSet());
    }

    public AbstractCommandData executorClassToCommand(Class<? extends IDevCommandExecutor> commandExecutorClass) {

        Class<? extends CommandArgument<?>>[] argsValidationTypes = null;

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
                .withMandatoryArguments(argsValidationTypes)
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
    private Class<? extends IDevCommandExecutor> getCommandExecutor(Class<?> classToCheck) {
        return isValidCommandClass(classToCheck) ? (Class<? extends IDevCommandExecutor>) classToCheck : null;
    }

    private boolean isValidCommandClass(Class<?> classToCheck) {
        return IDevCommandExecutor.class.isAssignableFrom(classToCheck);
    }

}
