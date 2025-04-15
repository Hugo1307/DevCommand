package dev.hugog.minecraft.dev_command.arguments;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class CommandArgument {
    private final String name;
    private final String description;
    private final int position;
    private final Class<? extends CommandArgumentParser<?>> validator;
    private final boolean optional;
}
