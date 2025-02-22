package blog.javamastery.datastructures.common;

/**
 * A generic hash table implementation that handles collisions through chaining.
 *
 * @param <K> the type of keys maintained by this hash table
 * @param <V> the type of mapped values
 */
public interface HashTable<K, V> extends Iterable<HashTable.Entry<K, V>> {

    /**
     * Associates the specified value with the specified key.
     * If the key already exists, the old value is replaced.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with the key, or null if there was no mapping
     * @throws IllegalArgumentException if the key is null
     */
    V put(K key, V value);

    /**
     * Returns the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if no mapping
     * @throws IllegalArgumentException if the key is null
     */
    V get(K key);

    /**
     * Removes the mapping for the specified key if present.
     *
     * @param key the key whose mapping is to be removed
     * @return the previous value associated with key, or null if no mapping
     * @throws IllegalArgumentException if the key is null
     */
    V remove(K key);

    /**
     * Returns true if this hash table contains a mapping for the specified key.
     *
     * @param key the key whose presence is to be tested
     * @return true if this map contains a mapping for the specified key
     * @throws IllegalArgumentException if the key is null
     */
    boolean containsKey(K key);

    /**
     * Returns the number of key-value mappings in this hash table.
     *
     * @return the number of key-value mappings
     */
    int size();

    /**
     * Returns true if this hash table contains no key-value mappings.
     *
     * @return true if this hash table contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * Removes all mappings from this hash table.
     */
    void clear();

    /**
     * Returns the current load factor of this hash table.
     *
     * @return the load factor (number of entries / table size)
     */
    double getLoadFactor();

    /**
     * Represents a key-value entry in the hash table.
     */
    interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);
    }
}
