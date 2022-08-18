package me.hgsoft.minecraft.devcommand.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class BukkitCommand extends AbstractCommand {

    private final String permission;
    private final Class<?>[] argumentTypes;

    public BukkitCommand(String alias, String description, String permission, Class<?>[] argumentTypes, Class<? extends ICommandExecutor> executor) {
        super(alias, description, executor);
        this.permission = permission;
        this.argumentTypes = argumentTypes;
    }

}
