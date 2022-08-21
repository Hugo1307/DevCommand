package me.hgsoft.minecraft.devcommand.factories;

import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class CommandFactory implements IObjectFactory<ICommandExecutor, AbstractCommand> {

    private final Object[] executorArgs;

    public CommandFactory(Object... executorArgs) {
        this.executorArgs = executorArgs;
    }

    @Override
    @SuppressWarnings("all")
    public ICommandExecutor generate(AbstractCommand abstractCommand) {

        Class<? extends ICommandExecutor> executor = abstractCommand.getExecutor();
        ICommandExecutor executorInstance;

        try {

            Constructor<? extends ICommandExecutor> executorConstructor;

            if (abstractCommand instanceof BukkitCommand) {
                executorConstructor = executor.getConstructor(BukkitCommand.class, CommandSender.class, String[].class);
                executorInstance = executorConstructor.newInstance(abstractCommand, executorArgs[0], Arrays.copyOfRange(executorArgs, 1, executorArgs.length, String[].class));
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
