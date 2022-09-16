package me.hgsoft.minecraft.devcommand.dependencies;

import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.dependencies.DependencyRegistry;

import java.util.Arrays;
import java.util.List;

public class DependencyHandler {

    private static DependencyHandler instance;

    private DependencyHandler() { }

    public void registerDependency(Integration integration, Object dependency) {
        DependencyRegistry.getInstance().add(integration, dependency);
    }
    public void registerDependencies(Integration integration, Object... dependencies) {
        Arrays.stream(dependencies).forEach(dependency -> registerDependency(integration, dependency));
    }

    public Object getDependencyInstance(Integration integration, Class<?> dependencyClass) {

        DependencyRegistry dependencyRegistry = DependencyRegistry.getInstance();
        List<Object> registeredDependencies = dependencyRegistry.getValues(integration);

        for (Object registeredDependency : registeredDependencies) {
            if (dependencyClass.isInstance(registeredDependency)) {
                return registeredDependency;
            }
        }

        return null;

    }

    public static DependencyHandler createOrGetInstance() {
        if (instance == null) {
            instance = new DependencyHandler();
        }
        return instance;
    }

}
