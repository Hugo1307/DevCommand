package me.hgsoft.minecraft.devcommand;

import lombok.extern.log4j.Log4j2;
import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.discovery.CommandDiscoveryService;
import me.hgsoft.minecraft.devcommand.exceptions.InvalidIntegrationException;
import me.hgsoft.minecraft.devcommand.factory.CommandFactory;
import me.hgsoft.minecraft.devcommand.factory.CommandFactoryImpl;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;

import java.util.*;

@Log4j2
public class CommandHandler {

    private static CommandHandler currentInstance;

    private CommandHandler() { }

    public void registerCommand(Integration integration, AbstractCommand command) {
        CommandRegistry.getInstance().add(integration, command);
    }

    public boolean executeCommandByAlias(Integration integration, String alias, Object... commandArgs) {

        CommandFactory commandFactory = new CommandFactoryImpl(commandArgs);
        CommandRegistry commandRegistry = CommandRegistry.getInstance();
        List<AbstractCommand> registeredCommandsForIntegration = commandRegistry.getValues(integration);

        if (registeredCommandsForIntegration == null) {
            return false;
        }

        for (AbstractCommand registeredCommand : registeredCommandsForIntegration) {

            if (registeredCommand.getAlias().equalsIgnoreCase(alias)) {
                commandFactory.generateExecutor(registeredCommand).execute();
                return true;
            }

        }

        return false;

    }

    public void initCommandsAutoConfiguration(Integration integration) {

        if (!checkIntegrationValidity(integration)) {
            throw new InvalidIntegrationException(String.format("The integration %s contained an invalid base package.", integration.getName()));
        }

        CommandDiscoveryService commandDiscoveryService = new CommandDiscoveryService(integration);

        commandDiscoveryService.getCommandExecutorClasses()
                .stream()
                .map(commandDiscoveryService::executorClassToCommand)
                .forEach(commandExecutor -> {
                    CommandRegistry.getInstance().add(integration, commandExecutor);
                    log.info(String.format("Loaded command '%s' from '%s'.", commandExecutor.getAlias(), integration.getName()));
                });

    }

    private boolean checkIntegrationValidity(Integration integration) {
        if (integration == null || integration.getBasePackage() == null) {
            log.warn("Unable to find base package for Integration - Commands Auto-Configuration stopped.");
            return false;
        }
        return true;
    }

    public static CommandHandler createOrGetInstance() {
        if (currentInstance == null) {
            currentInstance = new CommandHandler();
        }
        return currentInstance;
    }

}
