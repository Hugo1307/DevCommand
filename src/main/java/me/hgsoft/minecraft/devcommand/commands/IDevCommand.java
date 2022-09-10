package me.hgsoft.minecraft.devcommand.commands;

import java.util.List;

public interface IDevCommand {

    void execute();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();
    List<Object> getDependencies();

}
