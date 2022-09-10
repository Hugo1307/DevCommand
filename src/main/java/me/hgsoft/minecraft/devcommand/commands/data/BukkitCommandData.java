package me.hgsoft.minecraft.devcommand.commands.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.hgsoft.minecraft.devcommand.commands.IDevCommand;
import me.hgsoft.minecraft.devcommand.integration.Integration;
import me.hgsoft.minecraft.devcommand.validators.CommandArgument;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BukkitCommandData extends AbstractCommandData {

    private final String permission;
    private final Class<? extends CommandArgument<?>>[] mandatoryArguments;
    private final Class<? extends CommandArgument<?>>[] optionalArguments;

    public BukkitCommandData(String name, String alias, String description, Integration integration, Class<?>[] dependencies, Class<? extends IDevCommand> executor, String permission, Class<? extends CommandArgument<?>>[] mandatoryArguments, Class<? extends CommandArgument<?>>[] optionalArguments) {
        super(name, alias, description, integration, executor, dependencies);
        this.permission = permission;
        this.mandatoryArguments = mandatoryArguments;
        this.optionalArguments = optionalArguments;
    }

}
