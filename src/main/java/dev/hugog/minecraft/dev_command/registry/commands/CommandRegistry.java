package dev.hugog.minecraft.dev_command.registry.commands;

import com.google.inject.Singleton;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.IRegistry;

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
