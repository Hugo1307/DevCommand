package me.hgsoft.minecraft.devcommand.factories.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class StringArgument extends CommandArgument<String> {

    public StringArgument(String argument) {
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
