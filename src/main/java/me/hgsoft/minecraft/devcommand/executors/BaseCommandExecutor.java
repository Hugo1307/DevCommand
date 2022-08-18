package me.hgsoft.minecraft.devcommand.executors;

import lombok.Getter;

@Getter
public abstract class BaseCommandExecutor implements ICommandExecutor {

    private final Object[] args;

    public BaseCommandExecutor(Object[] args) {
        this.args = args;
    }

}
