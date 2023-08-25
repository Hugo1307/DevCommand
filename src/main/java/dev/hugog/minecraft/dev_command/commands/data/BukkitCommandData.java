package dev.hugog.minecraft.dev_command.commands.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.validators.CommandArgument;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BukkitCommandData extends AbstractCommandData {

    private final String permission;
    private final boolean isPlayerOnly;
    private final Class<? extends CommandArgument<?>>[] mandatoryArguments;
    private final Class<? extends CommandArgument<?>>[] optionalArguments;

    public BukkitCommandData(String name, String alias, String description, Integration integration, Class<?>[] dependencies, Class<? extends IDevCommand> executor, String permission, boolean isPlayerOnly, Class<? extends CommandArgument<?>>[] mandatoryArguments, Class<? extends CommandArgument<?>>[] optionalArguments) {
        super(name, alias, description, integration, executor, dependencies);
        this.permission = permission;
        this.isPlayerOnly = isPlayerOnly;
        this.mandatoryArguments = mandatoryArguments;
        this.optionalArguments = optionalArguments;
    }

}
