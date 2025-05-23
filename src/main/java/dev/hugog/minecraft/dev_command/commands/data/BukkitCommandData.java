package dev.hugog.minecraft.dev_command.commands.data;

import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.validation.AutoValidationData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BukkitCommandData extends AbstractCommandData {

    private final String permission;
    private final boolean isPlayerOnly;
    private final CommandArgument[] arguments;
    private final AutoValidationData autoValidationData;

    public BukkitCommandData(String name, String alias, String description, Integration integration, Class<?>[] dependencies, Class<? extends IDevCommand> executor, String permission, boolean isPlayerOnly, CommandArgument[] arguments, AutoValidationData autoValidationData) {
        super(name, alias, description, integration, executor, dependencies);
        this.permission = permission;
        this.isPlayerOnly = isPlayerOnly;
        this.arguments = arguments;
        this.autoValidationData = autoValidationData;
    }

}
