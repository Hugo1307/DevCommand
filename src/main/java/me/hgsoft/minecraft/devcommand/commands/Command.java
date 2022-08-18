package me.hgsoft.minecraft.devcommand.commands;

import lombok.*;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;

@ToString
@EqualsAndHashCode(callSuper = false)
public class Command extends AbstractCommand {

    public Command(String alias, String description, Class<? extends ICommandExecutor> executor) {
        super(alias, description, executor);
    }

}
