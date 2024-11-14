package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

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
    public Optional<Double> parse() {
        if (!isValid()) {
            return Optional.empty();
        }
        return Optional.of(Double.parseDouble(getArgument()));
    }

}
