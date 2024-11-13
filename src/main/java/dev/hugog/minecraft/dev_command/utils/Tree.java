package dev.hugog.minecraft.dev_command.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Tree<T> {

    private final Node<T> root;

    public Tree(T rootData) {
        root = new Node<>(rootData);
    }

    /**
     * Finds the last node in the path of the given sequence.
     *
     * <p>
     * The method allows us to find the last node in the tree for a path defined by a list of data elements. If the path
     * is not found at all, the method will return the root node.
     * <p>
     * For example, if we have a tree with the following structure:
     *
     * <pre>
     *     A
     *   B   C
     * D       E
     * </pre>
     *
     * And we look for the path [A, C, E], the method will return the node E. On the other hand, if we look for the path
     * [A, C, B], the method will return the node C, because the node B is not a child of C.
     *
     * @param sequence a list of data elements that define the path to find
     * @return the last node in the path
     */
    @NonNull
    public Node<T> findPath(List<T> sequence) {
        Node<T> lastNode = root;
        for (T data : sequence) {
            Node<T> node = findNode(lastNode, data);
            if (node == null) {
                return lastNode;
            }
            lastNode = node;
        }
        return lastNode;
    }

    /**
     * Finds a node in the tree that contains the given data.
     *
     * <p>
     * It uses a depth-first search to find the node that contains the given data.
     *
     * @param data the data to find
     * @return the node that contains the data, or null if the data is not found
     */
    public Node<T> findNode(Node<T> node, T data) {
        if (node.getData().equals(data)) {
            return node;
        }

        for (Node<T> child : node.getChildren()) {
            Node<T> found = findNode(child, data);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Getter
    @Setter
    public static class Node<T> {
        private final T data;
        private Node<T> parent;
        private final List<Node<T>> children;
        private Object extraData;

        public Node(T data) {
            this.data = data;
            this.children = new ArrayList<>();
        }

        /**
         * Adds a child to the node.
         *
         * @param child the child to add
         */
        public void addChild(Node<T> child) {
            child.parent = this;
            children.add(child);
        }

        /**
         * Checks if the node is a leaf.
         *
         * @return true if the node is a leaf, false otherwise
         */
        public boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * Gets the depth of the node in the tree.
         *
         * @return the depth of the node
         */
        public int getDepth() {
            if (parent == null) {
                return 0;
            }
            return parent.getDepth() + 1;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", parent=" + parent +
                    ", extraData=" + extraData +
                    '}';
        }
    }
}