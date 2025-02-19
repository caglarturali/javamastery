package blog.javamastery.datastructures.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Queue Operations")
class SimpleQueueTest {
    private SimpleQueue<String> queue;

    @BeforeEach
    void setUp() {
        queue = new SimpleQueue<>();
    }

    @Test
    @DisplayName("Queue should be empty when newly created")
    void newQueueShouldBeEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertNull(queue.peek());
    }

    @Test
    @DisplayName("Enqueue should add elements to the queue")
    void enqueueShouldAddElements() {
        // Act
        queue.enqueue("First");
        queue.enqueue("Second");

        // Assert
        assertEquals(2, queue.size());
        assertEquals("First", queue.peek());
        assertFalse(queue.isEmpty());
    }

    @Test
    @DisplayName("Dequeue should remove and return elements in FIFO order")
    void dequeueShouldRemoveElementsInOrder() {
        // Arrange
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");

        // Act & Assert
        assertEquals("First", queue.dequeue());
        assertEquals("Second", queue.dequeue());
        assertEquals("Third", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("Queue should support iteration")
    void shouldSupportIteration() {
        // Arrange
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");

        // Act
        StringBuilder result = new StringBuilder();
        for (var element : queue) {
            result.append(element).append(",");
        }

        // Assert
        assertEquals("First,Second,Third,", result.toString());
        // Verify queue is unchanged after iteration
        assertEquals(3, queue.size());
        assertEquals("First", queue.peek());
    }

    @Test
    @DisplayName("Queue should support stream operations")
    void shouldSupportStreamOperations() {
        // Arrange
        queue.enqueue("first");
        queue.enqueue("second");
        queue.enqueue("third");

        // Act
        List<String> upperCaseList = queue.stream()
                .map(String::toUpperCase)
                .toList();

        // Assert
        assertEquals(List.of("FIRST", "SECOND", "THIRD"), upperCaseList);
        // Verify queue is unchanged after iteration
        assertEquals(3, queue.size());
        assertEquals("first", queue.peek());
    }

    @Test
    @DisplayName("Dequeue on empty queue should return null")
    void dequeueShouldReturnNullOnEmptyQueue() {
        assertNull(queue.dequeue());
    }

    @Test
    @DisplayName("Iterator should throw NoSuchElementException when no more elements")
    void iteratorShouldThrowWhenNoMoreElements() {
        var iterator = queue.iterator();
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}