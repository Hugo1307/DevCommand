package me.hgsoft.minecraft.devcommand.dependencies;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.registry.dependencies.DependencyRegistry;

import java.util.Arrays;
import java.util.List;

@Singleton
public class DependencyHandler {

    private final DependencyRegistry dependencyRegistry;

    @Inject
    public DependencyHandler(DependencyRegistry dependencyRegistry) {
        this.dependencyRegistry = dependencyRegistry;
    }

    public void registerDependency(Integration integration, Object dependency) {
        dependencyRegistry.add(integration, dependency);
    }
    public void registerDependencies(Integration integration, Object... dependencies) {
        Arrays.stream(dependencies).forEach(dependency -> registerDependency(integration, dependency));
    }

    public Object getDependencyInstance(Integration integration, Class<?> dependencyClass) {

        List<Object> registeredDependencies = dependencyRegistry.getValues(integration);

        for (Object registeredDependency : registeredDependencies) {
            if (dependencyClass.isInstance(registeredDependency)) {
                return registeredDependency;
            }
        }

        return null;

    }

}
