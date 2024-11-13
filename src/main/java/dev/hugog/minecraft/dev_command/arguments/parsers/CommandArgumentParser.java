package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class CommandArgumentParser<T> implements ICommandArgumentParser<T> {

    private final String argument;

}
