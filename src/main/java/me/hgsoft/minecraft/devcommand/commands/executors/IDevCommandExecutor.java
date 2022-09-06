package me.hgsoft.minecraft.devcommand.commands.executors;

public interface IDevCommandExecutor {

    void execute();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();

}
