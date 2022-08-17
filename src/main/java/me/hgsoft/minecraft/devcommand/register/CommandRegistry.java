package me.hgsoft.minecraft.devcommand.register;

import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.integration.Integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry implements IRegistry<Integration, Command> {

    private static CommandRegistry instance;

    private final Map<Integration, List<Command>> registeredCommands;

    private CommandRegistry() {
        this.registeredCommands = new HashMap<>();
    }

    @Override
    public void add(Integration key, Command value) {
        if (registeredCommands.containsKey(key) && registeredCommands.get(key) != null) {
            registeredCommands.get(key).add(value);
        } else {
            registeredCommands.put(key, List.of(value));
        }
    }

    @Override
    public void remove(Integration key) {
        registeredCommands.remove(key);
    }

    @Override
    public List<Command> getValues(Integration key) {
        return registeredCommands.get(key);
    }

    @Override
    public void setValues(Integration key, List<Command> value) {
        registeredCommands.put(key, value);
    }

    public static CommandRegistry getInstance() {
        if (instance == null)
            instance = new CommandRegistry();
        return instance;
    }

}
