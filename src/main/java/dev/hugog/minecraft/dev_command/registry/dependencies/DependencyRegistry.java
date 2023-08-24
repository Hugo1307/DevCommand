package dev.hugog.minecraft.dev_command.registry.dependencies;

import com.google.inject.Singleton;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.IRegistry;

import java.util.*;

@Singleton
public class DependencyRegistry implements IRegistry<Integration, Object> {

    private final Map<Integration, List<Object>> registeredDependencies;

    public DependencyRegistry() {
        this.registeredDependencies = new HashMap<>();
    }

    @Override
    public void add(Integration key, Object value) {
        if (registeredDependencies.containsKey(key) && registeredDependencies.get(key) != null) {
            registeredDependencies.get(key).add(value);
        } else {
            registeredDependencies.put(key, new ArrayList<>(Collections.singletonList(value)));
        }
    }

    @Override
    public void remove(Integration key) {
        registeredDependencies.remove(key);
    }

    @Override
    public List<Object> getValues(Integration key) {
        return registeredDependencies.get(key);
    }

    @Override
    public void setValues(Integration key, List<Object> value) {
        registeredDependencies.put(key, value);
    }

}
