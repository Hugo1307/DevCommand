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

        validateIntegration(integration);

        initCommandsAutoConfiguration(integration, new CommandDiscoveryService(integration));

    }

    public void initCommandsAutoConfiguration(@NonNull Integration integration, CommandDiscoveryService commandDiscoveryService) {

        validateIntegration(integration);

        if (commandDiscoveryService.containsCommandsWithRepeatedAliases()) {
            throw new AutoConfigurationException("Unable to autoconfigure commands as there are commands with repeated aliases.");
        }

        commandDiscoveryService.getDiscoveredCommandsData().forEach(abstractCommand -> {
            if (abstractCommand != null) {
                registerCommand(integration, abstractCommand);
                log.info(String.format("Loaded command '%s' from '%s'.", abstractCommand.getAlias(), integration.getName()));
            } else {
                log.info("Failed to load at least one of the commands...");
            }
        });

    }

    private void validateIntegration(Integration integration) {
        if (!integration.isValid()) {
            throw new InvalidIntegrationException(String.format("The integration %s contained an invalid base package.", integration.getName()));
        }
    }

}
