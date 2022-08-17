package me.hgsoft.minecraft.devcommand.factory;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.hgsoft.minecraft.devcommand.commands.Command;
import me.hgsoft.minecraft.devcommand.executors.CommandExecutor;

import java.lang.reflect.Constructor;

/**
 * Command Factory
 *
 * Produces plugin's commands.
 *
 * @author Hugo1307
 */

@RequiredArgsConstructor
public class CommandFactoryImpl implements CommandFactory {

    private final Object[] executorArgs;

    @Override
    public CommandExecutor generateExecutor(Command command) {

        Class<? extends CommandExecutor> executor = command.getExecutor();
        CommandExecutor executorInstance;

        try {
            Constructor<? extends CommandExecutor> executorConstructor = executor.getConstructor(Object[].class);
            executorInstance = executorConstructor.newInstance(executorArgs);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }

        return executorInstance;

    }

}
