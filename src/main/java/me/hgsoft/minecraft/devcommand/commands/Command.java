package me.hgsoft.minecraft.devcommand.commands;

import lombok.Data;
import me.hgsoft.minecraft.devcommand.executors.CommandExecutor;

@Data
public class Command {

    private final String alias;
    private final String description;
    private final Class<? extends CommandExecutor> executor;

}
