package me.hgsoft.minecraft.devcommand.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DoubleArgument extends CommandArgument<Double> {

    public DoubleArgument(String argument) {
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
