package me.hgsoft.minecraft.devcommand.executors;

public interface ICommandExecutor {

    void execute();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();

}
