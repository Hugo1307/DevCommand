package me.hgsoft.minecraft.devcommand.commands;

import lombok.*;
import me.hgsoft.minecraft.devcommand.executors.CommandExecutor;

@ToString
@EqualsAndHashCode(callSuper = false)
public class Command extends AbstractCommand {

    public Command(String alias, String description, Class<? extends CommandExecutor> executor) {
        super(alias, description, executor);
    }

}
