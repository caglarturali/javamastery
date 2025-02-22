package blog.javamastery.datastructures.hash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Simple Hash Table operations")
class SimpleHashTableTest {

    private SimpleHashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new SimpleHashTable<>();
    }

    @Test
    @DisplayName("Put and get single value")
    void putAndGetSingleValue() {
        hashTable.put("one", 1);
        assertEquals(1, hashTable.get("one"));
    }

    @Test
    @DisplayName("Handle null key")
    void handleNullKey() {
        assertThrows(IllegalArgumentException.class, () -> hashTable.put(null, 1));
        assertThrows(IllegalArgumentException.class, () -> hashTable.get(null));
    }

    @Test
    @DisplayName("Update existing value")
    void updateExistingValue() {
        hashTable.put("test", 1);
        Integer oldValue = hashTable.put("test", 2);
        assertEquals(1, oldValue);
        assertEquals(2, hashTable.get("test"));
    }

    @Test
    @DisplayName("Handle collisions")
    void handleCollisions() {
        // These keys have the same hash code
        String key1 = "Aa";
        String key2 = "BB";

        // Verify they actually collide
        assertEquals(
                Math.abs(key1.hashCode() % 16),
                Math.abs(key2.hashCode() % 16)
        );

        hashTable.put(key1, 1);
        hashTable.put(key2, 2);

        assertEquals(1, hashTable.get(key1));
        assertEquals(2, hashTable.get(key2));
    }

    @Test
    @DisplayName("Get non-existent key returns null")
    void getNonExistentKey() {
        assertNull(hashTable.get("nonexistent"));
    }

    @Test
    @DisplayName("Verify resizing")
    void verifyResizing() {
        // Add enough entries to trigger resize
        for (int i = 0; i < 20; i++) {
            hashTable.put("key" + i, i);
        }

        // Verify all values are still accessible
        for (int i = 0; i < 20; i++) {
            assertEquals(i, hashTable.get("key" + i));
        }
    }

    @Test
    @DisplayName("Verify containsKey behavior")
    void verifyContainsKeyBehavior() {
        hashTable.put("existing", 1);

        assertTrue(hashTable.containsKey("existing"));
        assertFalse(hashTable.containsKey("nonexistent"));
        assertThrows(IllegalArgumentException.class, () -> hashTable.containsKey(null));
    }

    @Test
    @DisplayName("Remove elements from chain")
    void removeElementsFromChain() {
        // Add elements that will collide
        String key1 = "Aa";
        String key2 = "BB";
        String key3 = "Ca";

        hashTable.put(key1, 1);
        hashTable.put(key2, 2);
        hashTable.put(key3, 3);

        // Remove from middle
        assertEquals(2, hashTable.remove(key2));
        assertFalse(hashTable.containsKey(key2));
        assertTrue(hashTable.containsKey(key1));
        assertTrue(hashTable.containsKey(key3));

        // Remove from head
        assertEquals(1, hashTable.remove(key1));
        assertFalse(hashTable.containsKey(key1));
        assertTrue(hashTable.containsKey(key3));

        // Remove last element
        assertEquals(3, hashTable.remove(key3));
        assertFalse(hashTable.containsKey(key3));
    }

    @Test
    @DisplayName("Size should be zero when newly created")
    void sizeShouldBeZeroNew() {
        assertEquals(0, hashTable.size());
        assertTrue(hashTable.isEmpty());
    }

    @Test
    @DisplayName("Size shouldn't be zero when there are elements")
    void sizeShouldBeNonzero() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        assertEquals(2, hashTable.size());
        assertFalse(hashTable.isEmpty());
    }

    @Test
    @DisplayName("Clear should reset the array and size")
    void clearShouldReset() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        assertEquals(2, hashTable.size());
        assertFalse(hashTable.isEmpty());
        assertNotNull(hashTable.get("one"));

        hashTable.clear();

        assertEquals(0, hashTable.size());
        assertTrue(hashTable.isEmpty());
        assertNull(hashTable.get("one"));
    }

    @Test
    @DisplayName("Iterator should traverse all entries")
    void iteratorShouldTraverseAllEntries() {
        // Add some entries
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        // Track what we've seen
        Set<String> seenKeys = new HashSet<>();
        Set<Integer> seenValues = new HashSet<>();
        int count = 0;

        // Traverse with iterator
        for (var entry : hashTable) {
            seenKeys.add(entry.getKey());
            seenValues.add(entry.getValue());
            count++;
        }

        // Verify we saw everything
        assertEquals(3, count);
        assertTrue(seenKeys.containsAll(Arrays.asList("one", "two", "three")));
        assertTrue(seenValues.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    @DisplayName("Iterator should handle collisions")
    void iteratorShouldHandleCollisions() {
        // Add entries that will collide
        String key1 = "Aa";
        String key2 = "BB";
        hashTable.put(key1, 1);
        hashTable.put(key2, 2);

        // Track what we've seen
        Set<String> seenKeys = new HashSet<>();
        Set<Integer> seenValues = new HashSet<>();

        // Traverse with iterator
        var iterator = hashTable.iterator();
        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            var entry = iterator.next();
            seenKeys.add(entry.getKey());
            seenValues.add(entry.getValue());
        }

        // Verify we saw both entries
        assertEquals(2, seenKeys.size());
        assertTrue(seenKeys.containsAll(List.of(key1, key2)));
        assertTrue(seenValues.containsAll(List.of(1, 2)));
    }

    @Test
    @DisplayName("Iterator should throw NoSuchElementException")
    void iteratorShouldThrowWhenEmpty() {
        var iterator = hashTable.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

}