package dev.hugog.minecraft.dev_command.validation;

import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;

public interface IAutoValidationConfiguration {
    String getNoPermissionMessage(BukkitDevCommand command);
    String getInvalidArgumentsMessage(BukkitDevCommand command);
    String getInvalidSenderMessage(BukkitDevCommand command);
}
