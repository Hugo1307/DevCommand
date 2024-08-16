package dev.hugog.minecraft.dev_command.commands.handler;

import com.google.inject.Inject;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.discovery.CommandDiscoveryService;
import dev.hugog.minecraft.dev_command.exceptions.AutoConfigurationException;
import dev.hugog.minecraft.dev_command.exceptions.InvalidIntegrationException;
import dev.hugog.minecraft.dev_command.factories.CommandFactory;
import dev.hugog.minecraft.dev_command.factories.IObjectFactory;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.commands.CommandRegistry;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;

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

    public boolean executeCommand(Integration integration, String[] arguments, Object... extraData) {

        List<AbstractCommandData> registeredCommandsForIntegration = commandRegistry.getValues(integration);

        if (registeredCommandsForIntegration == null) {
            return false;
        }

        for (AbstractCommandData registeredCommand : registeredCommandsForIntegration) {

            String[] alias = registeredCommand.getAlias().split(" ");
            int aliasLength = alias.length;

            if (arguments.length < aliasLength) {
                continue;
            }

            boolean isAlias = true;
            for (int argumentIdx = 0; argumentIdx < aliasLength; argumentIdx++) {
                if (!arguments[argumentIdx].equalsIgnoreCase(alias[argumentIdx])) {
                    isAlias = false;
                    break;
                }
            }

            if (isAlias) {
                IObjectFactory<IDevCommand, AbstractCommandData> commandFactory = new CommandFactory(Arrays.copyOfRange(arguments, aliasLength, arguments.length), extraData);
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

    public List<AbstractCommandData> getRegisteredCommands(Integration integration) {
        return commandRegistry.getValues(integration);
    }

    private void validateIntegration(Integration integration) {
        if (!integration.isValid()) {
            throw new InvalidIntegrationException(String.format("The integration %s contained an invalid base package.", integration.getName()));
        }
    }

}
