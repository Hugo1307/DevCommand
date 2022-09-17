package me.hgsoft.minecraft.devcommand.commands.handler;

import com.google.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.discovery.CommandDiscoveryService;
import me.hgsoft.minecraft.devcommand.exceptions.AutoConfigurationException;
import me.hgsoft.minecraft.devcommand.exceptions.InvalidIntegrationException;
import me.hgsoft.minecraft.devcommand.commands.IDevCommand;
import me.hgsoft.minecraft.devcommand.factories.IObjectFactory;
import me.hgsoft.minecraft.devcommand.factories.CommandFactory;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.commands.CommandRegistry;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class CommandHandler {

    private final CommandRegistry commandRegistry;

    @Inject
    public CommandHandler(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public void registerCommand(Integration integration, AbstractCommandData command) {
        commandRegistry.add(integration, command);
    }

    public boolean executeCommandByAlias(Integration integration, String alias, Object... commandArgs) {

        IObjectFactory<IDevCommand, AbstractCommandData> commandFactory = new CommandFactory(commandArgs);
        List<AbstractCommandData> registeredCommandsForIntegration = commandRegistry.getValues(integration);

        if (registeredCommandsForIntegration == null) {
            return false;
        }

        for (AbstractCommandData registeredCommand : registeredCommandsForIntegration) {

            if (registeredCommand.getAlias().equalsIgnoreCase(alias)) {
                commandFactory.generate(registeredCommand).execute();
                return true;
            }

        }

        return false;

    }

    public void initCommandsAutoConfiguration(@NonNull Integration integration) {

        if (!integration.isValid()) {
            throw new InvalidIntegrationException(String.format("The integration %s contained an invalid base package.", integration.getName()));
        }

        CommandDiscoveryService commandDiscoveryService = new CommandDiscoveryService(integration);

        List<AbstractCommandData> discoveredAbstractCommandDataList = commandDiscoveryService.getCommandExecutorClasses()
                .stream()
                .map(commandDiscoveryService::executorClassToCommand)
                .collect(Collectors.toList());

        boolean hasRepeatedAliases = discoveredAbstractCommandDataList.stream()
                .map(AbstractCommandData::getAlias)
                .distinct()
                .count() != discoveredAbstractCommandDataList.size();

        if (hasRepeatedAliases) {
            throw new AutoConfigurationException("Unable to autoconfigure commands as there are commands with repeated aliases.");
        }

        discoveredAbstractCommandDataList.forEach(abstractCommand -> {
            registerCommand(integration, abstractCommand);
            log.info(String.format("Loaded command '%s' from '%s'.", abstractCommand.getAlias(), integration.getName()));
        });

    }

}
