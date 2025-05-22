package dev.hugog.minecraft.dev_command.validation;

import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;

public class DefaultAutoValidationConfiguration implements IAutoValidationConfiguration {
    @Override
    public String getNoPermissionMessage(BukkitDevCommand command) {
        return "You don't have permission to execute the command.";
    }

    @Override
    public String getInvalidArgumentsMessage(BukkitDevCommand command) {
        return "You have provided invalid arguments. The argument {0} on position {1} is invalid.";
    }

    @Override
    public String getInvalidSenderMessage(BukkitDevCommand command) {
        return "This command can only be executed by players.";
    }
}
