package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class IntegerArgumentParser extends CommandArgumentParser<Integer> {

    public IntegerArgumentParser(String argument) {
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
