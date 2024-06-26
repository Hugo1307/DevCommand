package dev.hugog.minecraft.dev_command.commands;

import java.util.List;

public interface IDevCommand {

    void execute();
    boolean hasPermissionToExecuteCommand();
    boolean hasValidArgs();
    boolean canSenderExecuteCommand();
    List<Object> getDependencies();
    Object getDependency(Class<?> dependencyClass);

}
