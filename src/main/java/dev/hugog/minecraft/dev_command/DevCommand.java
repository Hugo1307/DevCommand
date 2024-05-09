package dev.hugog.minecraft.dev_command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Getter;
import dev.hugog.minecraft.dev_command.commands.handler.CommandHandler;
import dev.hugog.minecraft.dev_command.dependencies.DependencyHandler;
import dev.hugog.minecraft.dev_command.injection.GuiceBinderModule;

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
