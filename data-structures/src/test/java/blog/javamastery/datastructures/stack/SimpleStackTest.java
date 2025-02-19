package blog.javamastery.datastructures.stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stack Operations")
class SimpleStackTest {
    private SimpleStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new SimpleStack<>();
    }

    @Test
    @DisplayName("Stack should be empty when newly created")
    void newStackShouldBeEmpty() {
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("Push operation should add elements correctly")
    void pushShouldAddElement() {
        stack.push("First");

        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
        assertEquals("First", stack.peek());
    }

    @Test
    @DisplayName("Pop operation should remove and return the last element")
    void popShouldRemoveAndReturnLastElement() {
        stack.push("First");
        stack.push("Second");

        assertEquals("Second", stack.pop());
        assertEquals("First", stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    @DisplayName("Pop on empty stack should return null")
    void popOnEmptyStackShouldReturnNull() {
        assertNull(stack.pop());
    }

    @Test
    @DisplayName("Stack should grow automatically when full")
    void stackShouldGrowWhenFull() {
        int initialCapacity = stack.capacity();

        // Fill the stack to trigger growth
        for (int i = 0; i < initialCapacity + 1; i++) {
            stack.push(String.valueOf(i));
        }

        assertTrue(stack.capacity() > initialCapacity);
        assertEquals(initialCapacity + 1, stack.size());
    }

    @Test
    @DisplayName("Stack should shrink when many elements are removed")
    void stackShouldShrinkWhenManyElementsRemoved() {
        // First fill the stack to trigger growth
        for (int i = 0; i < 20; i++) {
            stack.push(String.valueOf(i));
        }

        int fullCapacity = stack.capacity();

        // Now remove most elements
        while (stack.size() > 4) {
            stack.pop();
        }

        assertTrue(stack.capacity() < fullCapacity);
    }

    @Test
    @DisplayName("Iterator should return elements in LIFO order")
    void iteratorShouldReturnElementsInLIFOOrder() {
        String[] items = {"First", "Second", "Third"};
        for (String item : items) {
            stack.push(item);
        }

        var iterator = stack.iterator();
        assertEquals("Third", iterator.next());
        assertEquals("Second", iterator.next());
        assertEquals("First", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    @DisplayName("Iterator should throw NoSuchElementException when exhausted")
    void iteratorShouldThrowWhenExhausted() {
        stack.push("First");
        var iterator = stack.iterator();

        iterator.next(); // Get the only element
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @DisplayName("Stream operations should work correctly")
    void streamOperationsShouldWorkCorrectly() {
        stack.push("First");
        stack.push("Second");
        stack.push("Third");

        var list = stack.stream()
                .filter(s -> s.length() > 5)
                .toList();

        assertEquals(1, list.size());
        assertEquals("Second", list.getFirst());
    }

    @Test
    @DisplayName("Get operation should return correct element at index")
    void getShouldReturnCorrectElement() {
        stack.push("First");
        stack.push("Second");

        assertEquals("First", stack.get(0));
        assertEquals("Second", stack.get(1));
    }

    @Test
    @DisplayName("Get with invalid index should throw exception")
    void getWithInvalidIndexShouldThrow() {
        stack.push("First");

        assertThrows(IndexOutOfBoundsException.class, () -> stack.get(1));
    }
}