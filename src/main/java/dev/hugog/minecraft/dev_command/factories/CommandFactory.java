package dev.hugog.minecraft.dev_command.factories;

import dev.hugog.minecraft.dev_command.commands.IDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;

public class CommandFactory implements IObjectFactory<IDevCommand, AbstractCommandData> {

    private final Object[] executorArgs;

    public CommandFactory(Object... executorArgs) {
        this.executorArgs = executorArgs;
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

                if (executorArgs.length == 0) {
                    throw new IllegalArgumentException("You must provide a CommandSender and a String[] as arguments for the BukkitCommandData executor.");
                }

                if (executorArgs[0] != null && !(executorArgs[0] instanceof CommandSender)) {
                    throw new IllegalArgumentException("The first argument for the BukkitCommandData executor must be a CommandSender.");
                }

                String[] remainingArgs = new String[0];

                if (executorArgs.length > 1) {
                    remainingArgs = (String[]) executorArgs[1];
                }

                executorInstance = executorConstructor.newInstance(abstractCommandData, executorArgs[0], remainingArgs);

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
