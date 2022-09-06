package me.hgsoft.minecraft.devcommand.factories;

import me.hgsoft.minecraft.devcommand.commands.data.AbstractCommandData;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import me.hgsoft.minecraft.devcommand.commands.executors.IDevCommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class CommandFactory implements IObjectFactory<IDevCommandExecutor, AbstractCommandData> {

    private final Object[] executorArgs;

    public CommandFactory(Object... executorArgs) {
        this.executorArgs = executorArgs;
    }

    @Override
    @SuppressWarnings("all")
    public IDevCommandExecutor generate(AbstractCommandData abstractCommandData) {

        Class<? extends IDevCommandExecutor> executor = abstractCommandData.getExecutor();
        IDevCommandExecutor executorInstance;

        try {

            Constructor<? extends IDevCommandExecutor> executorConstructor;

            if (abstractCommandData instanceof BukkitCommandData) {
                executorConstructor = executor.getConstructor(BukkitCommandData.class, CommandSender.class, String[].class);
                executorInstance = executorConstructor.newInstance(abstractCommandData, executorArgs[0], Arrays.copyOfRange(executorArgs, 1, executorArgs.length, String[].class));
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
