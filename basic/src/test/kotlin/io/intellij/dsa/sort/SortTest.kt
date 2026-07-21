package io.intellij.dsa.sort

import io.intellij.dsa.getLogger
import io.intellij.dsa.sort.impl.InsertSort
import io.intellij.dsa.sort.impl.MergeSort
import io.intellij.dsa.sort.impl.QuickSort
import io.intellij.dsa.sort.impl.SelectSort
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * SortTest
 *
 * @author dev@intellij.io
 * @since 2025-05-30
 */
class SortTest {

  companion object {
    private val log = getLogger(SortTest::class.java)
  }

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
    val sortResult = sortArr(sort, createRandomArray(100000, 100000))
    log.info("$sortResult")
    Assertions.assertTrue(sortResult.sorted)
    Assertions.assertTrue(sortResult.same)
  }

}
