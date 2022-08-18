package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.factory.CommandFactory;
import me.hgsoft.minecraft.devcommand.factory.CommandFactoryImpl;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;

import java.util.*;

public class CommandHandler {

    private static CommandHandler currentInstance;

    private CommandHandler() { }

    public void registerCommand(Integration integration, AbstractCommand command) {
        CommandRegistry.getInstance().add(integration, command);
    }

    public boolean executeCommandByAlias(Integration integration, String alias, Object[] commandArgs) {

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

    public static CommandHandler createOrGetInstance() {

        if (currentInstance == null) {
            currentInstance = new CommandHandler();
        }

        return currentInstance;

    }

}
