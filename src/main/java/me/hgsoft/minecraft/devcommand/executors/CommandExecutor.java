package me.hgsoft.minecraft.devcommand.executors;

import lombok.Getter;

@Getter
public abstract class CommandExecutor implements ICommandExecutor {

    private final Object[] args;

    public CommandExecutor(Object[] args) {
        this.args = args;
    }

}
