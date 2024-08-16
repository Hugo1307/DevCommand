package dev.hugog.minecraft.dev_command.arguments.validators;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class CommandArgumentValidator<T> implements ICommandArgumentValidator<T> {

    private final String argument;

}
