package dev.hugog.minecraft.dev_command.commands.builder;

import dev.hugog.minecraft.dev_command.commands.IDevCommand;

public interface ICommandBuilder<T extends ICommandBuilder<T, C>, C> {

    T withName(String name);
    T withAlias(String alias);
    T withDescription(String description);
    T withExecutor(Class<? extends IDevCommand> executor);

    C build();

}
