package me.hgsoft.minecraft.devcommand.factory;

import me.hgsoft.minecraft.devcommand.commands.AbstractCommand;
import me.hgsoft.minecraft.devcommand.commands.BukkitCommand;
import me.hgsoft.minecraft.devcommand.commands.BaseCommand;
import me.hgsoft.minecraft.devcommand.executors.ICommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Command Factory
 *
 * Produces plugin's commands.
 *
 * @author Hugo1307
 */

public class CommandFactoryImpl implements CommandFactory {

    private final Object[] executorArgs;

    public CommandFactoryImpl(Object... executorArgs) {
        this.executorArgs = executorArgs;
    }

    @Override
    public ICommandExecutor generateExecutor(AbstractCommand abstractCommand) {

        Class<? extends ICommandExecutor> executor = abstractCommand.getExecutor();
        ICommandExecutor executorInstance;

        try {

            Constructor<? extends ICommandExecutor> executorConstructor;

            if (abstractCommand instanceof BaseCommand) {
                executorConstructor = executor.getConstructor(Object[].class);
                executorInstance = executorConstructor.newInstance(executorArgs);
            } else if (abstractCommand instanceof BukkitCommand) {
                executorConstructor = executor.getConstructor(CommandSender.class, String[].class);
                executorInstance = executorConstructor.newInstance((CommandSender) executorArgs[0], (String[]) Arrays.copyOfRange(executorArgs, 1, executorArgs.length)[0]);
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
