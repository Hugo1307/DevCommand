package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.annotations.Argument;
import dev.hugog.minecraft.dev_command.arguments.CommandArgument;
import dev.hugog.minecraft.dev_command.arguments.parsers.CommandArgumentParser;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

class ArgumentFactoryTest {

    private IObjectFactory<CommandArgument, Argument> argumentFactory;
    private Argument argument;

    @BeforeEach
    void setUp() {
        argumentFactory = new ArgumentFactory();
        argument = new Argument() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Argument.class;
            }

            @Override
            public String name() {
                return "test";
            }

            @Override
            public String description() {
                return "Test Argument";
            }

            @Override
            public int position() {
                return 0;
            }

            @Override
            public Class<? extends CommandArgumentParser<?>> parser() {
                return IntegerArgumentParser.class;
            }

            @Override
            public boolean optional() {
                return false;
            }
        };
    }

    @Test
    void generateExecutorForBukkitCommand() {
        CommandArgument generatedCommandArgument = argumentFactory.generate(argument);
        Assertions.assertThat(generatedCommandArgument)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "test")
                .hasFieldOrPropertyWithValue("description", "Test Argument")
                .hasFieldOrPropertyWithValue("position", 0)
                .hasFieldOrPropertyWithValue("validator", IntegerArgumentParser.class)
                .hasFieldOrPropertyWithValue("optional", false);
    }


}