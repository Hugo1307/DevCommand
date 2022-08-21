package me.hgsoft.minecraft.devcommand.factories.validators;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class CommandArgument<T> implements ICommandArgument<T> {

    private final String argument;

}
