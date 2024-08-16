package dev.hugog.minecraft.dev_command.arguments.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DoubleArgumentValidator extends CommandArgumentValidator<Double> {

    public DoubleArgumentValidator(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return getArgument().matches("[-+]?\\d*\\.\\d+");
    }

    @Override
    public Double parse() {
        return Double.parseDouble(getArgument());
    }

}
