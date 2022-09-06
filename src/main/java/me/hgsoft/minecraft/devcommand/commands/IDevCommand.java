package me.hgsoft.minecraft.devcommand.commands;

public interface IDevCommand {

    void execute();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();

}
