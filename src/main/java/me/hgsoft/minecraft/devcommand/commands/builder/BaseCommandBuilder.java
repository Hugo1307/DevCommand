package me.hgsoft.minecraft.devcommand.commands.builder;

import me.hgsoft.minecraft.devcommand.commands.BaseCommand;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;

public class BaseCommandBuilder implements ICommandBuilder<BaseCommandBuilder, BaseCommand> {

    private String alias;
    private String description;
    private Class<? extends ICommandExecutor> executor;

    public BaseCommandBuilder(String alias, Class<? extends ICommandExecutor> executor) {
        this.alias = alias;
        this.executor = executor;
    }

    @Override
    public BaseCommandBuilder withAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public BaseCommandBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public BaseCommandBuilder withExecutor(Class<? extends ICommandExecutor> executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public BaseCommand build() {
        return new BaseCommand(alias, description, executor);
    }

}
