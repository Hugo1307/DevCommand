package me.hgsoft.minecraft.devcommand.commands.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.hgsoft.minecraft.devcommand.commands.executors.IDevCommandExecutor;
import me.hgsoft.minecraft.devcommand.validators.CommandArgument;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BukkitCommandData extends AbstractCommandData {

    private final String permission;
    private final Class<? extends CommandArgument<?>>[] mandatoryArguments;
    private final Class<? extends CommandArgument<?>>[] optionalArguments;

    public BukkitCommandData(String name, String alias, String description, String permission, Class<? extends CommandArgument<?>>[] mandatoryArguments, Class<? extends CommandArgument<?>>[] optionalArguments, Class<? extends IDevCommandExecutor> executor) {
        super(name, alias, description, executor);
        this.permission = permission;
        this.mandatoryArguments = mandatoryArguments;
        this.optionalArguments = optionalArguments;
    }

}
