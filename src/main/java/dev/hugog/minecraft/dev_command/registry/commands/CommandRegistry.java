package dev.hugog.minecraft.dev_command.registry.commands;

import com.google.inject.Singleton;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.integration.Integration;
import dev.hugog.minecraft.dev_command.registry.IRegistry;
import dev.hugog.minecraft.dev_command.utils.Tree;

import java.util.*;

@Singleton
public class CommandRegistry implements IRegistry<Integration, AbstractCommandData> {

    private final Map<Integration, List<AbstractCommandData>> registeredCommands;
    private final Map<Integration, Tree<String>> commandTrees;

    public CommandRegistry() {
        this.registeredCommands = new HashMap<>();
        this.commandTrees = new HashMap<>();
    }

    @Override
    public void add(Integration key, AbstractCommandData value) {
        if (registeredCommands.containsKey(key) && registeredCommands.get(key) != null) {
            registeredCommands.get(key).add(value);
        } else {
            registeredCommands.put(key, new ArrayList<>(List.of(value)));
        }
    }

    @Override
    public void remove(Integration key) {
        registeredCommands.remove(key);
    }

    @Override
    public List<AbstractCommandData> getValues(Integration key) {
        return registeredCommands.get(key);
    }

    /**
     * Gets the command tree for the given integration.
     *
     * <p>
     * The leaf nodes of the tree contain an {@link AbstractCommandData} object, representing the command that should
     * be executed when the path to that node is followed.
     *
     * @param integration the integration to get the command tree for
     * @return the command tree for the given integration
     */
    public Tree<String> getCommandTree(Integration integration) {
        return commandTrees.computeIfAbsent(integration, k -> buildCommandTree(integration));
    }

    @Override
    public void setValues(Integration key, List<AbstractCommandData> value) {
        registeredCommands.put(key, value);
    }

    /**
     * Builds a tree of commands for the given integration.
     *
     * <p>
     * The leaf nodes of the tree contain an {@link AbstractCommandData} object, representing the command that should
     * be executed when the path to that node is followed.
     *
     * @param integration the integration to build the command tree for
     * @return the tree of commands for the given integration
     */
    private Tree<String> buildCommandTree(Integration integration) {
        Tree<String> commandTree = new Tree<>(integration.getName());
        if (!registeredCommands.containsKey(integration)) {
            return commandTree;
        }

        for (AbstractCommandData registeredCommand : registeredCommands.get(integration)) {
            String[] alias = registeredCommand.getAlias().split(" ");
            Tree.Node<String> lastNode = null;

            for (int i = 0; i < alias.length; i++) {
                // Get an existing node with the prefix of the current alias, if not found, get the root node
                Tree.Node<String> existingNode = commandTree.findPath(Arrays.asList(alias).subList(0, i));
                Tree.Node<String> newNode = new Tree.Node<>(alias[i]);

                // Add the new node to the existing node (either the root node or a node with the prefix of the current alias)
                existingNode.addChild(newNode);
                lastNode = newNode;
            }

            // Add the command data to the last node in the path which is the leaf node
            if (lastNode != null) {
                lastNode.setExtraData(registeredCommand);
            }
        }

        return commandTree;
    }

}
