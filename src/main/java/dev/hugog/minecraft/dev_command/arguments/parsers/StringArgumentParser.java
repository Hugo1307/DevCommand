package dev.hugog.minecraft.dev_command.arguments.parsers;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class StringArgumentParser extends CommandArgumentParser<String> {

    public StringArgumentParser(String argument) {
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
