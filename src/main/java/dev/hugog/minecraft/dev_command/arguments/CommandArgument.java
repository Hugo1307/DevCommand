package dev.hugog.minecraft.dev_command.arguments;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;

public record CommandArgument(String name, String description, int position,
                              Class<? extends CommandArgumentParser<?>> validator,
                              boolean optional
) { }
