package dev.hugog.minecraft.dev_command.arguments.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class IntegerArgumentValidator extends CommandArgumentValidator<Integer> {

    public IntegerArgumentValidator(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return getArgument().matches("[-+]?\\d+");
    }

    @Override
    public Integer parse() {
        return Integer.parseInt(getArgument());
    }

}
