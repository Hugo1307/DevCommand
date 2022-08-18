package me.hgsoft.minecraft.devcommand.factory;


import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;

public interface CommandFactory {
    ICommandExecutor generateExecutor(AbstractCommand command);
}
