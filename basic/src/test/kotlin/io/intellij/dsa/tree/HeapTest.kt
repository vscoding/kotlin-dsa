package io.intellij.dsa.tree

import io.intellij.dsa.tree.heap.Heap
import io.intellij.dsa.tree.heap.HeapImpl
import io.intellij.dsa.tree.heap.HeapType
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * HeapTest
 *
 * @author dev@intellij.io
 * @since 2025-05-31
 */
class HeapTest {

  private val values = arrayOf(9, 1, 5, 1, -3, 8, 0)

  @Test
  fun `min heap extracts values in ascending order`() {
    val heap: Heap<Int> = HeapImpl()
    values.forEach(heap::add)

    assertEquals(values.size, heap.size())
    assertEquals(-3, heap.peek())

    val extracted = List(values.size) { heap.extract() }
    println("Min heap extraction: $extracted")

    assertContentEquals(values.sorted(), extracted)
    assertTrue(heap.isEmpty())
    assertNull(heap.extract())
  }

  @Test
  fun `heapify builds a min heap from existing values`() {
    val heap: Heap<Int> = HeapImpl(values)

    val extracted = List(values.size) { heap.extract() }
    println("Heapify extraction: $extracted")

    assertContentEquals(values.sorted(), extracted)
    assertTrue(heap.isEmpty())
  }

  @Test
  fun `max heap extracts values in descending order`() {
    val heap: Heap<Int> = HeapImpl(values, HeapType.MAX_HEAP)

    val extracted = List(values.size) { heap.extract() }
    println("Max heap extraction: $extracted")

    assertContentEquals(values.sortedDescending(), extracted)
    assertTrue(heap.isEmpty())
  }

}
