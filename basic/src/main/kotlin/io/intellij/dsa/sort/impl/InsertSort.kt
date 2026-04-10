package io.intellij.dsa.sort.impl

import io.intellij.dsa.sort.Sort
import io.intellij.dsa.sort.swap

/**
 * InsertSort 插入排序
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
class InsertSort<T : Comparable<T>> : Sort<T> {

  override fun sort(arr: Array<T>) {
    if (arr.isEmpty() || arr.size == 1) return
    val n = arr.size
    for (i in 1 until n) {
      for (j in i downTo 1) {
        if (arr[j] < arr[j - 1]) {
          arr.swap(j, j - 1)
        }
      }
    }
  }

}