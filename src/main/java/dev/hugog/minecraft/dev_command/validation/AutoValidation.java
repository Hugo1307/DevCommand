package dev.hugog.minecraft.dev_command.validation;

public record AutoValidation(
        boolean validatePermission,
        boolean validateArguments,
        boolean validateSender
) { }
