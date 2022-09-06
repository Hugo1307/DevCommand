package me.hgsoft.minecraft.devcommand.commands.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.hgsoft.minecraft.devcommand.commands.IDevCommand;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class AbstractCommandData {

    private final String name;
    private final String alias;
    private final String description;
    private final Class<? extends IDevCommand> executor;

}
