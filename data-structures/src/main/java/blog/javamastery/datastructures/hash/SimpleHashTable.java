package blog.javamastery.datastructures.hash;

import blog.javamastery.datastructures.common.HashTable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleHashTable<K, V> implements HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    // Array of linked entries for chaining
    private Entry<K, V>[] table;

    // Number of key-value mappings in the table
    private int size;

    // The load factor threshold for resizing
    private final double loadFactor;

    /**
     * Entry class for chaining collision resolution
     */
    private static class Entry<K, V> implements HashTable.Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    @SuppressWarnings("unchecked")
    public SimpleHashTable() {
        this.table = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        // Check if we need to resize
        if ((size + 1.0) / table.length > loadFactor) {
            resize();
        }

        int index = hash(key);

        // If no entry exists at this index, create new entry
        if (table[index] == null) {
            table[index] = new Entry<>(key, value, null);
            size++;
            return null;
        }

        // Handle collision by traversing the chain
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            // If key exists, update value
            if (current.key.equals(key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            prev = current;
            current = current.next;
        }

        // Add new entry to the chain
        if (prev != null) {
            prev.next = new Entry<>(key, value, null);
            size++;
        }
        return null;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[oldTable.length * 2];
        size = 0; // The call to put will increment it below

        // Rehash all entries
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);

        if (table[index] == null) {
            return null;
        }

        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                V oldValue = current.value;
                if (prev == null) {
                    // It's the first element in the chain
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return oldValue;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        this.table = (Entry<K, V>[]) new Entry[this.table.length];
        this.size = 0;
    }

    @Override
    public double getLoadFactor() {
        return (double) size / table.length;
    }

    @Override
    public Iterator<HashTable.Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int arrayIndex = 0;
            private Entry<K, V> current = null;

            private void advanceToNextEntry() {
                // If we have more elements in current chain, stay there
                if (current != null && current.next != null) {
                    current = current.next;
                    return;
                }

                // Find next non-null bucket
                current = null;
                while (arrayIndex < table.length) {
                    if (table[arrayIndex] != null) {
                        current = table[arrayIndex];
                        arrayIndex++;
                        return;
                    }
                    arrayIndex++;
                }
            }

            @Override
            public boolean hasNext() {
                if (current == null) {
                    advanceToNextEntry();
                }
                return current != null;
            }

            @Override
            public HashTable.Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Entry<K, V> entry = current;
                advanceToNextEntry();
                return entry;
            }
        };
    }
}
