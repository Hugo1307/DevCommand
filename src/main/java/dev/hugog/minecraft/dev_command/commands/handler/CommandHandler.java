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
import dev.hugog.minecraft.dev_command.utils.Tree;
import dev.hugog.minecraft.dev_command.validation.DefaultAutoValidationConfiguration;
import dev.hugog.minecraft.dev_command.validation.IAutoValidationConfiguration;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.bukkit.command.CommandSender;

import java.util.*;

@Log4j2
public class CommandHandler {

    private final CommandRegistry commandRegistry;
    private IAutoValidationConfiguration autoValidationConfiguration;

    @Inject
    public CommandHandler(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
        this.autoValidationConfiguration = new DefaultAutoValidationConfiguration();
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

            if (arguments.length == 0 && registeredCommand.getAlias().isEmpty()) {
                IObjectFactory<IDevCommand, AbstractCommandData> commandFactory = new CommandFactory(arguments, extraData);
                IDevCommand command = commandFactory.generate(registeredCommand);
                if (command.performAutoValidation(autoValidationConfiguration)) {
                    command.execute();
                }
                return true;
            } else if (arguments.length < aliasLength) {
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
                IDevCommand command = commandFactory.generate(registeredCommand);
                if (command.performAutoValidation(autoValidationConfiguration)) {
                    command.execute();
                }
                return true;
            }

        }

        return false;

    }

    public List<String> executeTabComplete(Integration integration, CommandSender commandSender, String[] arguments) {
        Tree<String> commandTree = commandRegistry.getCommandTree(integration);
        Tree.Node<String> lastArgumentNode = commandTree.findPath(Arrays.asList(arguments).subList(0, arguments.length - 1));

        if (lastArgumentNode.isLeaf()) { // The tab completion should be handled by the user-defined command
            String[] commandArguments = Arrays.copyOfRange(arguments, lastArgumentNode.getDepth(), arguments.length);
            IObjectFactory<IDevCommand, AbstractCommandData> commandFactory = new CommandFactory(commandArguments, commandSender);
            IDevCommand command = commandFactory.generate((AbstractCommandData) lastArgumentNode.getExtraData());
            return command.onTabComplete(commandArguments);
        } else { // The tab completion should be handled by the DevCommand using info. about the command tree
            return lastArgumentNode.getChildren().stream().map(Tree.Node::getData).toList();
        }
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
                log.info("Loaded command '{}' from '{}'.", abstractCommand.getAlias(), integration.getName());
            } else {
                log.info("Failed to load at least one of the commands...");
            }
        });

    }

    public List<AbstractCommandData> getRegisteredCommands(Integration integration) {
        return commandRegistry.getValues(integration);
    }

    public void useAutoValidationConfiguration(IAutoValidationConfiguration configuration) {
        this.autoValidationConfiguration = configuration;
    }

    private void validateIntegration(Integration integration) {
        if (!integration.isValid()) {
            throw new InvalidIntegrationException(String.format("The integration %s contained an invalid base package.", integration.getName()));
        }
    }

}
