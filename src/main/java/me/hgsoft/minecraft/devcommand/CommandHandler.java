package me.hgsoft.minecraft.devcommand;

import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.factory.CommandFactory;
import me.hgsoft.minecraft.devcommand.factory.CommandFactoryImpl;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.register.CommandRegistry;

import java.util.*;

public class CommandHandler {

    private static CommandHandler currentInstance;

    private CommandHandler() { }

    public void registerCommand(Integration integration, Command command) {
        CommandRegistry.getInstance().add(integration, command);
    }

    public boolean executeCommandByAlias(Integration integration, String alias, Object[] commandArgs) {

        CommandFactory commandFactory = new CommandFactoryImpl(commandArgs);
        CommandRegistry commandRegistry = CommandRegistry.getInstance();
        List<Command> registeredCommandsForIntegration = commandRegistry.getValues(integration);

        if (registeredCommandsForIntegration == null) {
            return false;
        }

        for (Command registeredCommand : registeredCommandsForIntegration) {

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
