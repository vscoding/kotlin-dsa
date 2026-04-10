package io.intellij.dsa.uf

/**
 * UnionFind
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
interface UnionFind<T> {

  fun isEmpty(): Boolean {
    return size() == 0
  }

  fun size(): Int

  fun contains(value: T): Boolean

  fun add(value: T): Boolean

  fun union(value1: T, value2: T): Boolean

  fun isConnected(value1: T, value2: T): Boolean

  fun clear()

}