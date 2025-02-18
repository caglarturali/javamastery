package blog.javamastery.datastructures.tree;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> {
    public enum TraversalOrder {
        INORDER,
        PREORDER,
        POSTORDER
    }

    private Node root;

    private class Node {
        T value;
        Node left;
        Node right;

        Node(T value) {
            this.value = value;
        }
    }

    public void insert(T value) {
        root = insertRec(root, value);
    }

    public boolean contains(T value) {
        return containsRec(root, value);
    }

    public void delete(T value) {
        root = deleteRec(root, value);
    }

    public List<T> traverse(TraversalOrder order) {
        var result = new ArrayList<T>();
        switch (order) {
            case INORDER -> traverseInOrder(root, result);
            case PREORDER -> traversePreOrder(root, result);
            case POSTORDER -> traversePostOrder(root, result);
        }
        return result;
    }

    public int size() {
        return sizeRec(root);
    }

    public int height() {
        return heightRec(root);
    }

    public T min() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    public T max() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public boolean isValidBST() {
        return isValidBST(root, null, null);
    }

    private Node insertRec(Node node, T value) {
        if (node == null) {
            return new Node(value);
        }

        int comparison = value.compareTo(node.value);
        if (comparison < 0) {
            node.left = insertRec(node.left, value);
        } else if (comparison > 0) {
            node.right = insertRec(node.right, value);
        }

        return node;
    }

    private boolean containsRec(Node node, T value) {
        if (node == null) {
            return false;
        }

        int comparison = value.compareTo(node.value);
        if (comparison == 0) {
            return true;
        }
        return comparison < 0
                ? containsRec(node.left, value)
                : containsRec(node.right, value);
    }

    private Node deleteRec(Node node, T value) {
        if (node == null) {
            return null;
        }

        int comparison = value.compareTo(node.value);
        if (comparison < 0) {
            node.left = deleteRec(node.left, value);
        } else if (comparison > 0) {
            node.right = deleteRec(node.right, value);
        } else {
            // Case 1 & 2: no child or one child
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Case 3: two children
            // Find the smallest value in the right subtree (successor)
            node.value = findMin(node.right);
            // Delete the successor
            node.right = deleteRec(node.right, node.value);
        }
        return node;
    }

    private T findMin(Node node) {
        T minValue = node.value;
        while (node.left != null) {
            minValue = node.left.value;
            node = node.left;
        }
        return minValue;
    }

    private int sizeRec(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRec(node.left) + sizeRec(node.right);
    }

    private int heightRec(Node node) {
        if (node == null) {
            return -1; // Empty tree
        }
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }

    private boolean isValidBST(Node node, T min, T max) {
        if (node == null) {
            return true;
        }

        if ((min != null && node.value.compareTo(min) <= 0) ||
                (max != null && node.value.compareTo(max) >= 0)) {
            return false;
        }

        return isValidBST(node.left, min, node.value) &&
                isValidBST(node.right, node.value, max);
    }

    private void traverseInOrder(Node node, List<T> result) {
        if (node != null) {
            traverseInOrder(node.left, result);
            result.add(node.value);
            traverseInOrder(node.right, result);
        }
    }

    private void traversePreOrder(Node node, List<T> result) {
        if (node != null) {
            result.add(node.value);
            traversePreOrder(node.left, result);
            traversePreOrder(node.right, result);
        }
    }

    private void traversePostOrder(Node node, List<T> result) {
        if (node != null) {
            traversePostOrder(node.left, result);
            traversePostOrder(node.right, result);
            result.add(node.value);
        }
    }
}
