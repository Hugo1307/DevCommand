package dev.hugog.minecraft.dev_command.arguments.parsers;

import java.util.Optional;

public interface ICommandArgumentParser<T> {

    /**
     * Checks if the argument is valid
     *
     * @return true if the argument is valid, false otherwise
     */
    boolean isValid();

    /**
     * Parses the argument into the desired type
     *
     * @return an {@link Optional} containing the parsed argument, or an empty {@link Optional} if the argument is invalid
     */
    Optional<T> parse();

}
