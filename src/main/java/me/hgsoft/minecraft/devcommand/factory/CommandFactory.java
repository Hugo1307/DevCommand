package me.hgsoft.minecraft.devcommand.factory;


import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.executors.CommandExecutor;

public interface CommandFactory {
    CommandExecutor generateExecutor(Command command);
}
