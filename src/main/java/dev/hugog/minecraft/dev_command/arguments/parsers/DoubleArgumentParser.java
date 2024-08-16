package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DoubleArgumentParser extends CommandArgumentParser<Double> {

    public DoubleArgumentParser(String argument) {
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
