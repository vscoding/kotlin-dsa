package io.intellij.dsa.sort

import io.intellij.dsa.sort.impl.InsertSort
import io.intellij.dsa.sort.impl.MergeSort
import io.intellij.dsa.sort.impl.QuickSort
import io.intellij.dsa.sort.impl.SelectSort
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * SortTest
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
class SortTest {

  @Test
  fun `test select sort`() {
    test(SelectSort())
  }

  @Test
  fun `test insert sort`() {
    test(InsertSort())
  }

  @Test
  fun `test merge sort`() {
    test(MergeSort())
  }

  @Test
  fun `test quick sort`() {
    test(QuickSort())
  }

  private fun test(sort: Sort<Int>) {
    val result = sortArr(sort, createRandomArray(100000, 100000))
    println(result)
    Assertions.assertTrue(result.sorted)
    Assertions.assertTrue(result.same)
  }

}
