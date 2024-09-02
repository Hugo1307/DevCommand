package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.annotations.Argument;
import dev.hugog.minecraft.dev_command.arguments.CommandArgument;

public class ArgumentFactory implements IObjectFactory<CommandArgument, Argument> {

    @Override
    public CommandArgument generate(Argument command) {
        return new CommandArgument(command.name(), command.description(), command.position(),
                command.parser(), command.optional());
    }

}
