package me.hgsoft.minecraft.devcommand.factories.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class IntegerArgument extends CommandArgument<Integer> {

    public IntegerArgument(String argument) {
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
