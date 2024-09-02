package dev.hugog.minecraft.dev_command.commands.builder;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.validation.AutoValidationData;

public class BukkitCommandDataBuilder implements ICommandBuilder<BukkitCommandDataBuilder, BukkitCommandData> {

    private String name;
    private String alias;
    private String description;
    private Integration integration;
    private Class<? extends IDevCommand> executor;
    private Class<?>[] dependencies;
    private String permission;
    private boolean isPlayerOnly;
    private CommandArgument[] arguments;
    private AutoValidationData autoValidationData;

    public BukkitCommandDataBuilder(String alias, Integration integration, Class<? extends IDevCommand> executor) {
        this.alias = alias;
        this.integration = integration;
        this.executor = executor;
    }

    @Override
    public BukkitCommandDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public BukkitCommandDataBuilder withAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public BukkitCommandDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public BukkitCommandDataBuilder withExecutor(Class<? extends IDevCommand> executor) {
        this.executor = executor;
        return this;
    }

    public BukkitCommandDataBuilder withPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public BukkitCommandDataBuilder withArguments(CommandArgument... arguments) {
        this.arguments = arguments;
        return this;
    }

    public final BukkitCommandDataBuilder withDependencies(Class<?>... dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public final BukkitCommandDataBuilder withIntegration(Integration integration) {
        this.integration = integration;
        return this;
    }

    public final BukkitCommandDataBuilder withPlayerOnly(boolean isPlayerOnly) {
        this.isPlayerOnly = isPlayerOnly;
        return this;
    }

    public final BukkitCommandDataBuilder withAutoValidation(AutoValidationData autoValidationData) {
        this.autoValidationData = autoValidationData;
        return this;
    }

    public BukkitCommandData build() {
        return new BukkitCommandData(name, alias, description, integration, dependencies, executor, permission,
                isPlayerOnly, arguments, autoValidationData);
    }

}
