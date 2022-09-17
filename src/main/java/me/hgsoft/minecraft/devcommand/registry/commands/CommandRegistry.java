package me.hgsoft.minecraft.devcommand.registry.commands;

import com.google.inject.Singleton;
import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.IRegistry;

import java.util.*;

@Singleton
public class CommandRegistry implements IRegistry<Integration, AbstractCommandData> {

    private final Map<Integration, List<AbstractCommandData>> registeredCommands;

    public CommandRegistry() {
        this.registeredCommands = new HashMap<>();
    }

    @Override
    public void add(Integration key, AbstractCommandData value) {
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
    public List<AbstractCommandData> getValues(Integration key) {
        return registeredCommands.get(key);
    }

    @Override
    public void setValues(Integration key, List<AbstractCommandData> value) {
        registeredCommands.put(key, value);
    }

}
