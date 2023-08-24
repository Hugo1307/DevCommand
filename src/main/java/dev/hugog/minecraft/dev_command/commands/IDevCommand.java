package dev.hugog.minecraft.dev_command.commands;

import java.util.List;

public interface IDevCommand {

    void execute();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();
    List<Object> getDependencies();

}
