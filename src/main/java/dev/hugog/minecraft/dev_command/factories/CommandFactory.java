package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;

public class CommandFactory implements IObjectFactory<IDevCommand, AbstractCommandData> {

    private final String[] args;
    private final Object[] extraData;

    public CommandFactory(String[] args, Object... extraData) {
        this.args = args;
        this.extraData = extraData;
    }

    @Override
    @SuppressWarnings("all")
    public IDevCommand generate(AbstractCommandData abstractCommandData) {

        Class<? extends IDevCommand> executor = abstractCommandData.getExecutor();
        IDevCommand executorInstance;

        try {

            Constructor<? extends IDevCommand> executorConstructor;

            if (abstractCommandData instanceof BukkitCommandData) {
                executorConstructor = executor.getConstructor(BukkitCommandData.class, CommandSender.class, String[].class);

                if (extraData.length == 0) {
                    throw new IllegalArgumentException("You must provide at least a CommandSender as arguments for the BukkitCommandData executor.");
                }

                if (extraData[0] != null && !(extraData[0] instanceof CommandSender)) {
                    throw new IllegalArgumentException("The first argument for the BukkitCommandData executor must be a CommandSender.");
                }

                CommandSender commandSender = (CommandSender) extraData[0];
                executorInstance = executorConstructor.newInstance(abstractCommandData, commandSender, args);

            } else {
                executorInstance = null;
            }

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

        return executorInstance;

    }

}
