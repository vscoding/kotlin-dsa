package io.intellij.dsa.sort

import io.intellij.dsa.getLogger
import io.intellij.dsa.sort.impl.InsertSort
import io.intellij.dsa.sort.impl.MergeSort
import io.intellij.dsa.sort.impl.QuickSort
import io.intellij.dsa.sort.impl.SelectSort
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

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
  fun `select sort orders all values`() {
    test(SelectSort())
  }

  @Test
  fun `insert sort orders all values`() {
    test(InsertSort())
  }

  @Test
  fun `merge sort orders all values`() {
    test(MergeSort())
  }

  @Test
  fun `quick sort orders all values`() {
    test(QuickSort())
  }

  private fun test(sort: Sort<Int>) {
    val sortResult = sortArr(sort, arrayOf(7, -3, 7, 0, 12, -3, 1))
    log.info("$sortResult")
    assertTrue(sortResult.sorted)
    assertTrue(sortResult.same)
  }

}
