package dev.hugog.minecraft.dev_command.arguments.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class StringArgumentValidator extends CommandArgumentValidator<String> {

    public StringArgumentValidator(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String parse() {
        return getArgument();
    }

}
