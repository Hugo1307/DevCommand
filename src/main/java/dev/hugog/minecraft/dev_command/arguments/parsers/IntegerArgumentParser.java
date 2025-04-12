package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

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
    public Optional<Integer> parse() {
        if (!isValid()) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(getArgument()));
    }

}
