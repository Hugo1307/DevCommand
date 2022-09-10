package me.hgsoft.minecraft.devcommand.dependencies;

import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.dependencies.DependencyRegistry;

import java.util.List;

public class DependencyHandler {

    private static DependencyHandler instance;

    private DependencyHandler() { }

    public void registerDependency(Integration integration, Object dependency) {
        DependencyRegistry.getInstance().add(integration, dependency);
    }

    public Object getDependencyInstance(Integration integration, Class<?> dependencyClass) {

        DependencyRegistry dependencyRegistry = DependencyRegistry.getInstance();
        List<Object> registeredDependencies = dependencyRegistry.getValues(integration);

        for (Object registeredDependency : registeredDependencies) {
            if (dependencyClass.isInstance(dependencyClass)) {
                return registeredDependency;
            }
        }

        return null;

    }

    public static DependencyHandler getInstance() {
        if (instance == null) {
            instance = new DependencyHandler();
        }
        return instance;
    }

}
