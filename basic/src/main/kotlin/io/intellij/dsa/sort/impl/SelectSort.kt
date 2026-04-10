package io.intellij.dsa.sort.impl

import io.intellij.dsa.sort.Sort
import io.intellij.dsa.sort.swap

/**
 * SelectSort 选择排序
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
class SelectSort<T : Comparable<T>> : Sort<T> {

  override fun sort(arr: Array<T>) {
    if (arr.isEmpty() || arr.size == 1) return
    val n = arr.size
    for (i in 0 until n) {
      for (j in i + 1 until n) {
        if (arr[j] < arr[i]) {
          arr.swap(i, j)
        }
      }
    }
  }

}