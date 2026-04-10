package io.intellij.dsa.sort

/**
 * Sort
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
interface Sort<T : Comparable<T>> {

  /**
   * Sorts the given array in ascending order.
   *
   * @param arr the array to be sorted
   */
  fun sort(arr: Array<T>)

}

fun <T : Comparable<T>> Array<T>.swap(i: Int, j: Int) {
  if (i == j) return
  val temp = this[i]
  this[i] = this[j]
  this[j] = temp
}
