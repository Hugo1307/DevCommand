package me.hgsoft.minecraft.devcommand.register;

import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;

import java.util.*;

public class CommandRegistry implements IRegistry<Integration, AbstractCommand> {

    private static CommandRegistry instance;

    private final Map<Integration, List<AbstractCommand>> registeredCommands;

    private CommandRegistry() {
        this.registeredCommands = new HashMap<>();
    }

    @Override
    public void add(Integration key, AbstractCommand value) {
        if (registeredCommands.containsKey(key) && registeredCommands.get(key) != null) {
            registeredCommands.get(key).add(value);
        } else {
            registeredCommands.put(key, new ArrayList<>(List.of(value)));
        }
    }

    @Override
    public void remove(Integration key) {
        registeredCommands.remove(key);
    }

    @Override
    public List<AbstractCommand> getValues(Integration key) {
        return registeredCommands.get(key);
    }

    @Override
    public void setValues(Integration key, List<AbstractCommand> value) {
        registeredCommands.put(key, value);
    }

    public static CommandRegistry getInstance() {
        if (instance == null)
            instance = new CommandRegistry();
        return instance;
    }

}
