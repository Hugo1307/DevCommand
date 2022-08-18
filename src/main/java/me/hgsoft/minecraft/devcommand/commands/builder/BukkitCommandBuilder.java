package me.hgsoft.minecraft.devcommand.commands.builder;

import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;

public class BukkitCommandBuilder implements ICommandBuilder<BukkitCommandBuilder, BukkitCommand> {

    private String alias;
    private String description;
    private String permission;
    private Class<?>[] argumentTypes;
    private Class<? extends ICommandExecutor> executor;

    public BukkitCommandBuilder(String alias, Class<? extends ICommandExecutor> executor) {
        this.alias = alias;
        this.executor = executor;
    }

    @Override
    public BukkitCommandBuilder withAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public BukkitCommandBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public BukkitCommandBuilder withExecutor(Class<? extends ICommandExecutor> executor) {
        this.executor = executor;
        return this;
    }

    public BukkitCommandBuilder withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public BukkitCommandBuilder withArgumentTypes(Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes;
        return this;
    }

    public BukkitCommand build() {
        return new BukkitCommand(alias, description, permission, argumentTypes, executor);
    }

}
