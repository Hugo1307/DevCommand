package dev.hugog.minecraft.dev_command.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class AutoValidationData {
    private final boolean validatePermission;
    private final boolean validateArguments;
    private final boolean validateSender;
}


