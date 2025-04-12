package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BooleanArgumentParser extends CommandArgumentParser<Boolean> {

    public BooleanArgumentParser(String argument) {
        super(argument);
    }

    @Override
    public boolean isValid() {
        return getArgument() != null && Arrays.stream(new String[] {"true", "false", "yes", "no"})
                .anyMatch(b -> b.equalsIgnoreCase(getArgument()));
    }

    @Override
    public Optional<Boolean> parse() {
        String[] trueTokens = new String[] {"true", "yes", "enabled"};
        String[] falseTokens = new String[] {"false", "no", "disabled"};

        if (!isValid()) {
            return Optional.empty();
        }

        if (Arrays.stream(trueTokens).anyMatch(s -> s.equalsIgnoreCase(getArgument()))) {
            return Optional.of(true);
        } else if (Arrays.stream(falseTokens).anyMatch(s -> s.equalsIgnoreCase(getArgument()))) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

}
