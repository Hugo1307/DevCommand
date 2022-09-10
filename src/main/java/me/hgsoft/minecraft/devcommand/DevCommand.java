package me.hgsoft.minecraft.devcommand;

import lombok.Getter;
import me.hgsoft.minecraft.devcommand.dependencies.DependencyHandler;

@Getter
public final class DevCommand {

    private static DevCommand instance;

    private final CommandHandler commandHandler;
    private final DependencyHandler dependencyHandler;

    private DevCommand() {
        this.commandHandler = CommandHandler.createOrGetInstance();
        this.dependencyHandler = DependencyHandler.createOrGetInstance();
    }

    public static DevCommand getInstance() {
        if (instance == null) {
            instance = new DevCommand();
        }
        return instance;
    }

}
