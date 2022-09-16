package me.hgsoft.minecraft.devcommand.registry.dependencies;

import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.IRegistry;

import javax.annotation.Nullable;
import java.util.*;

public class DependencyRegistry implements IRegistry<Integration, Object> {

    private static DependencyRegistry instance;

    private final Map<Integration, List<Object>> registeredDependencies;

    private DependencyRegistry() {
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

    public static DependencyRegistry getInstance() {
        if (instance == null) {
            instance = new DependencyRegistry();
        }
        return instance;
    }

}
