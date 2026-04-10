package io.intellij.dsa.sort.impl

import io.intellij.dsa.sort.Sort

/**
 * MergeSort
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
class MergeSort<T : Comparable<T>> : Sort<T> {

  override fun sort(arr: Array<T>) {
    if (arr.isEmpty() || arr.size == 1) return
    this.divide(arr, 0, arr.size - 1)
  }

  private fun divide(array: Array<T>, left: Int, right: Int) {
    if (left >= right) return

    val mid = (left + right) / 2
    divide(array, left, mid)
    divide(array, mid + 1, right)
    merge(array, left, mid, right)
  }

  private fun merge(array: Array<T>, left: Int, mid: Int, right: Int) {
    val size = right - left + 1
    val tmp: Array<Comparable<T>> = Array(size) { array[left] }

    var x = left
    var y = mid + 1
    var tmpIndex = 0

    // 处理左右两个子数组，直到其中一个处理完
    while (x <= mid && y <= right) {
      tmp[tmpIndex++] = if (array[x] <= array[y]) {
        array[x++]
      } else {
        array[y++]
      }
    }

    // 处理左子数组剩余元素
    while (x <= mid) {
      tmp[tmpIndex++] = array[x++]
    }

    // 处理右子数组剩余元素
    while (y <= right) {
      tmp[tmpIndex++] = array[y++]
    }
    // 将临时数组中的元素复制回原数组
    System.arraycopy(tmp, 0, array, left, size)
  }

}