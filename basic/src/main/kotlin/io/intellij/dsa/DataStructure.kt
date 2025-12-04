package io.intellij.dsa

/**
 * Key-Value DataStructure 统一定义 key value 的统一操作
 */
interface KVOperator<K, V> {

    /**
     * Checks if the data structure contains no elements.
     *
     * @return `true` if the data structure is empty, `false` otherwise.
     */
    fun isEmpty(): Boolean = size() == 0

    /**
     * Returns the number of key-value pairs present in the data structure.
     *
     * @return The total count of key-value pairs in the data structure.
     */
    fun size(): Int

    /**
     * Checks if the specified key exists in the data structure.
     *
     * @param key The key to look for in the data structure.
     * @return `true` if the key exists, `false` otherwise.
     */
    fun contains(key: K): Boolean

    /**
     * Retrieves the value associated with the specified key from the data structure.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or `null` if the key does not exist.
     */
    fun get(key: K): V?

    /**
     * Inserts a key-value pair into the data structure. If the key already exists, the value is updated with the new value.
     *
     * @param key The key to be inserted or updated.
     * @param value The value associated with the key.
     */
    fun insert(key: K, value: V)

    /**
     * Removes the entry associated with the specified key from the data structure.
     *
     * @param key The key whose associated entry is to be removed.
     * @return The value of the removed entry, or `null` if the key does not exist.
     */
    fun remove(key: K): V?

    /**
     * 清空
     */
    fun clear()
}

interface DataStructurePrintable {
    fun print() {
        TODO("Not yet implemented")
    }
}