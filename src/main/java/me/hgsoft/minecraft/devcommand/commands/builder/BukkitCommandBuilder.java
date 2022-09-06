package me.hgsoft.minecraft.devcommand.commands.builder;

import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.executors.IDevCommandExecutor;
import me.hgsoft.minecraft.devcommand.validators.CommandArgument;

public class BukkitCommandBuilder implements ICommandBuilder<BukkitCommandBuilder, BukkitCommandData> {

    private String name;
    private String alias;
    private String description;
    private String permission;
    private Class<? extends CommandArgument<?>>[] mandatoryArguments;
    private Class<? extends CommandArgument<?>>[] optionalArguments;
    private Class<? extends IDevCommandExecutor> executor;

    public BukkitCommandBuilder(String alias, Class<? extends IDevCommandExecutor> executor) {
        this.alias = alias;
        this.executor = executor;
    }

    @Override
    public BukkitCommandBuilder withName(String name) {
        this.name = name;
        return this;
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
    public BukkitCommandBuilder withExecutor(Class<? extends IDevCommandExecutor> executor) {
        this.executor = executor;
        return this;
    }

    public BukkitCommandBuilder withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @SafeVarargs
    public final BukkitCommandBuilder withMandatoryArguments(Class<? extends CommandArgument<?>>... argumentTypes) {
        this.mandatoryArguments = argumentTypes;
        return this;
    }

    @SafeVarargs
    public final BukkitCommandBuilder withOptionalArguments(Class<? extends CommandArgument<?>>... argumentTypes) {
        this.optionalArguments = argumentTypes;
        return this;
    }

    public BukkitCommandData build() {
        return new BukkitCommandData(name, alias, description, permission, mandatoryArguments, optionalArguments, executor);
    }

}
