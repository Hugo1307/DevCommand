package me.hgsoft.minecraft.devcommand.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class AbstractCommand {

    private final String name;
    private final String alias;
    private final String description;
    private final Class<? extends ICommandExecutor> executor;

}
