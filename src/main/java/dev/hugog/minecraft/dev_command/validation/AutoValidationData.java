package dev.hugog.minecraft.dev_command.validation;

public record AutoValidationData(
        boolean validatePermission,
        boolean validateArguments,
        boolean validateSender
) { }
