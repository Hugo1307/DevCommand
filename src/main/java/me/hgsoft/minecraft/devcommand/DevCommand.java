package me.hgsoft.minecraft.devcommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Getter;
import me.hgsoft.minecraft.devcommand.commands.handler.CommandHandler;
import me.hgsoft.minecraft.devcommand.dependencies.DependencyHandler;
import me.hgsoft.minecraft.devcommand.injection.GuiceBinderModule;

@Getter
public final class DevCommand {

    private static DevCommand instance;

    @Inject
    private CommandHandler commandHandler;
    @Inject
    private DependencyHandler dependencyHandler;

    private DevCommand() {
        initDependencyInjectionModules();
    }

    public static DevCommand getOrCreateInstance() {
        if (instance == null) {
            instance = new DevCommand();
        }
        return instance;
    }

    private void initDependencyInjectionModules() {
        GuiceBinderModule guiceBinderModule = new GuiceBinderModule();
        Injector injector = guiceBinderModule.createInjector();
        injector.injectMembers(this);
    }

}
