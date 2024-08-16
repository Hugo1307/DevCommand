package dev.hugog.minecraft.dev_command.arguments;

import dev.hugog.minecraft.dev_command.arguments.validators.CommandArgumentValidator;

import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandArgument {

    private final String name;
    private final String description;
    private final int position;
    private final Class<? extends CommandArgumentValidator<?>> validator;
    private final boolean optional;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandArgument that = (CommandArgument) o;
        return position == that.position && optional == that.optional && Objects.equals(name,
                that.name) && Objects.equals(description, that.description)
                && Objects.equals(validator, that.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, position, validator, optional);
    }
}
