package dev.hugog.minecraft.dev_command.arguments;

import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class CommandArgument {
    private final String name;
    private final String description;
    private final int position;
    private final Class<? extends CommandArgumentParser<?>> validator;
    private final boolean optional;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommandArgument that = (CommandArgument) o;
        return position == that.position && optional == that.optional && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(validator, that.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, position, validator, optional);
    }
}
