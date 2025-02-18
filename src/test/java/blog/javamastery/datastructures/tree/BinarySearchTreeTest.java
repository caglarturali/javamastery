package blog.javamastery.datastructures.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Binary Search Tree Operations")
class BinarySearchTreeTest {
    private BinarySearchTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new BinarySearchTree<>();
    }

    @Test
    @DisplayName("Tree should be able to insert and find values")
    void shouldInsertAndFindValue() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);

        assertTrue(tree.contains(5));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(7));
        assertFalse(tree.contains(4));
    }

    @Test
    @DisplayName("Size should reflect number of nodes in tree")
    void sizeShouldReflectNodeCount() {
        assertEquals(0, tree.size());

        tree.insert(5);
        assertEquals(1, tree.size());

        tree.insert(3);
        tree.insert(7);
        assertEquals(3, tree.size());

        tree.delete(3);
        assertEquals(2, tree.size());
    }

    @Test
    @DisplayName("Height should reflect the longest path from root to leaf")
    void heightShouldReflectLongestPath() {
        assertEquals(-1, tree.height());  // Empty tree

        tree.insert(5);
        assertEquals(0, tree.height());   // Single node

        tree.insert(3);
        tree.insert(7);
        assertEquals(1, tree.height());   // Perfect tree of height 1

        tree.insert(8);
        assertEquals(2, tree.height());   // Right path is longer
    }

    @Test
    @DisplayName("Min should return smallest value in tree")
    void minShouldReturnSmallestValue() {
        assertThrows(IllegalStateException.class, () -> tree.min());

        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);

        assertEquals(1, tree.min());
    }

    @Test
    @DisplayName("Max should return largest value in tree")
    void maxShouldReturnLargestValue() {
        assertThrows(IllegalStateException.class, () -> tree.max());

        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(9);

        assertEquals(9, tree.max());
    }

    @Test
    @DisplayName("isValidBST should verify BST properties")
    void isValidBSTShouldVerifyProperties() {
        assertTrue(tree.isValidBST());  // Empty tree is valid

        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);
        tree.insert(6);
        tree.insert(8);

        assertTrue(tree.isValidBST());
    }

    @Test
    @DisplayName("In-order traversal should return values in sorted order")
    void inOrderTraversalShouldReturnSortedValues() {
        // Create a tree:
        //       5
        //      / \
        //     3   7
        //    / \   \
        //   1   4   9
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);
        tree.insert(9);

        var result = tree.traverse(BinarySearchTree.TraversalOrder.INORDER);
        assertEquals(List.of(1, 3, 4, 5, 7, 9), result);
    }

    @Test
    @DisplayName("Pre-order traversal should visit root before children")
    void preOrderTraversalShouldVisitRootFirst() {
        // Same tree structure as above
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);
        tree.insert(9);

        var result = tree.traverse(BinarySearchTree.TraversalOrder.PREORDER);
        assertEquals(List.of(5, 3, 1, 4, 7, 9), result);
    }

    @Test
    @DisplayName("Post-order traversal should visit children before root")
    void postOrderTraversalShouldVisitChildrenFirst() {
        // Same tree structure as above
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);
        tree.insert(4);
        tree.insert(9);

        var result = tree.traverse(BinarySearchTree.TraversalOrder.POSTORDER);
        assertEquals(List.of(1, 4, 3, 9, 7, 5), result);
    }

    @Test
    @DisplayName("Deleting a leaf node should maintain BST properties")
    void deletingLeafNodeShouldMaintainBSTProperties() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(1);

        tree.delete(1);  // Delete leaf node

        assertFalse(tree.contains(1));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(7));

        assertEquals(List.of(3, 5, 7), tree.traverse(BinarySearchTree.TraversalOrder.INORDER));
    }

    @Test
    @DisplayName("Deleting node with one child should maintain BST properties")
    void deletingNodeWithOneChildShouldMaintainBSTProperties() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(9);

        tree.delete(7);  // Delete node with one child

        assertFalse(tree.contains(7));
        assertTrue(tree.contains(9));
        assertEquals(List.of(3, 5, 9), tree.traverse(BinarySearchTree.TraversalOrder.INORDER));
    }

    @Test
    @DisplayName("Deleting node with two children should maintain BST properties")
    void deletingNodeWithTwoChildrenShouldMaintainBSTProperties() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.insert(6);
        tree.insert(8);

        tree.delete(7);  // Delete node with two children

        assertFalse(tree.contains(7));
        assertTrue(tree.contains(8));
        assertTrue(tree.contains(6));
        assertEquals(List.of(3, 5, 6, 8), tree.traverse(BinarySearchTree.TraversalOrder.INORDER));
    }

    @Test
    @DisplayName("Deleting root node should maintain BST properties")
    void deletingRootNodeShouldMaintainBSTProperties() {
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);

        tree.delete(5);  // Delete root

        assertFalse(tree.contains(5));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(7));
        assertEquals(List.of(3, 7), tree.traverse(BinarySearchTree.TraversalOrder.INORDER));
    }
}