package io.intellij.project.dsa

import io.intellij.project.dsa.cache.bloom.BloomFilter
import io.intellij.project.dsa.cache.bloom.BloomFilterImpl
import io.intellij.project.dsa.cache.lru.Lru
import io.intellij.project.dsa.cache.lru.LruImpl

/**
 * Creates and returns an instance of an LRU (Least Recently Used) cache with the specified maximum size.
 *
 * @param maxSize The maximum capacity of the LRU cache. When the number of elements exceeds this size,
 * the least recently used items will be evicted.
 * @return An instance of [Lru] implementing the LRU cache behavior.
 */
fun <K, V> buildLRU(maxSize: Int): Lru<K, V> {
    return LruImpl(maxSize)
}

/**
 * Builds and returns a Bloom filter initialized with the specified expected number of insertions.
 * This Bloom filter is used for efficient membership testing with a trade-off of allowing false positives.
 *
 * @param expectedInsertions The estimated number of elements to be inserted into the Bloom filter.
 * @return A newly created instance of a Bloom filter.
 */
fun buildBloomFilter(expectedInsertions: Int): BloomFilter {
    return BloomFilterImpl(expectedInsertions)
}
