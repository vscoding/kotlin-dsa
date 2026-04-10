package io.intellij.dsa.tree.heap

/**
 * Heap
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
interface Heap<T> {

  fun isEmpty(): Boolean {
    return size() == 0
  }

  fun size(): Int

  fun add(value: T)

  fun peek(): T?

  fun extract(): T?

  fun getType(): HeapType

  fun clear()
}

enum class HeapType {
  MIN_HEAP,
  MAX_HEAP
}