package me.hgsoft.minecraft.devcommand.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;
import me.hgsoft.minecraft.devcommand.factories.validators.CommandArgument;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BukkitCommand extends AbstractCommand {

    private final String permission;
    private final Class<? extends CommandArgument<?>>[] mandatoryArguments;
    private final Class<? extends CommandArgument<?>>[] optionalArguments;

    public BukkitCommand(String name, String alias, String description, String permission, Class<? extends CommandArgument<?>>[] mandatoryArguments, Class<? extends CommandArgument<?>>[] optionalArguments, Class<? extends ICommandExecutor> executor) {
        super(name, alias, description, executor);
        this.permission = permission;
        this.mandatoryArguments = mandatoryArguments;
        this.optionalArguments = optionalArguments;
    }

}
