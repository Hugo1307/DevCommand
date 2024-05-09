package dev.hugog.minecraft.dev_command.commands.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class AbstractCommandData {

    private final String name;
    private final String alias;
    private final String description;
    private final Integration integration;
    private final Class<? extends IDevCommand> executor;
    private final Class<?>[] dependencies;

}
