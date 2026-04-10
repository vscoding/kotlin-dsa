package io.intellij.dsa.tree

import io.intellij.dsa.sort.createRandomArray
import io.intellij.dsa.tree.heap.Heap
import io.intellij.dsa.tree.heap.HeapImpl
import io.intellij.dsa.tree.heap.HeapType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * HeapTest
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class HeapTest {

  private fun Array<Int>.sortThen(): Array<Int> {
    this.sort()
    return this
  }

  @Test
  fun `test min heap`() {
    val heap: Heap<Int> = HeapImpl()

    val array = createRandomArray(100000, 100000)
    array.forEach { heap.add(it) }

    array.copyOf().sortThen().forEach { Assertions.assertEquals(it, heap.extract()) }
  }

  @Test
  fun `test heapify`() {
    val array = createRandomArray(100000, 100000)
    val heap: Heap<Int> = HeapImpl(array)

    array.copyOf().sortThen().forEach { Assertions.assertEquals(it, heap.extract()) }
  }

  @Test
  fun `test max heap reverse`() {
    val array = createRandomArray(100000, 100000)
    val heap: Heap<Int> = HeapImpl(array, HeapType.MAX_HEAP) { a, b -> b.compareTo(a) }

    array.copyOf().sortThen().forEach { Assertions.assertEquals(it, heap.extract()) }
  }

}