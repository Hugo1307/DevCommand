package me.hgsoft.minecraft.devcommand.validators;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BooleanArgument extends CommandArgument<Boolean> {

    public BooleanArgument(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return getArgument() != null && Arrays.stream(new String[] {"true", "false", "yes", "no"})
                .anyMatch(b -> b.equalsIgnoreCase(getArgument()));
    }

    @Override
    public Boolean parse() {
        String[] trueTokens = new String[] {"true", "yes", "enabled"};
        String[] falseTokens = new String[] {"false", "no", "disabled"};
        if (Arrays.stream(trueTokens).anyMatch(s -> s.equalsIgnoreCase(getArgument()))) {
            return true;
        } else if (Arrays.stream(falseTokens).anyMatch(s -> s.equalsIgnoreCase(getArgument()))) {
            return false;
        } else {
            return null;
        }
    }

}
