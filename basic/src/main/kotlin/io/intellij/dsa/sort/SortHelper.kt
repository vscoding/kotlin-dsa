package io.intellij.dsa.sort

import com.google.common.base.Stopwatch

fun createRandomArray(size: Int, range: Int): Array<Int> {
    return Array(size) { (0..range).random() }
}

private fun sameArray(array1: Array<*>, array2: Array<*>): Boolean {
    if (array1.size != array2.size) return false
    for (i in array1.indices) {
        if (array1[i] != array2[i]) return false
    }
    return true
}

private fun <T : Comparable<T>> sorted(array: Array<T>): Boolean {
    for (i in 0 until array.size - 1) {
        if (array[i] > array[i + 1]) {
            return false
        }
    }
    return true
}

fun <T : Comparable<T>> sortArr(sort: Sort<T>, array: Array<T>): SortResult {
    val copyOf = array.copyOf()
    copyOf.sort()

    val stopwatch = Stopwatch.createStarted()
    sort.sort(array)
    stopwatch.stop()

    return SortResult(
        type = sort.javaClass,
        costTime = stopwatch.elapsed(java.util.concurrent.TimeUnit.MILLISECONDS),
        same = sameArray(array, copyOf),
        sorted = sorted(array)
    )
}

data class SortResult(val type: Class<*>, val costTime: Long, val same: Boolean, val sorted: Boolean) {
    override fun toString(): String {
        return """
            Sort Method  : ${type.name}
            Cost Time(ms): $costTime
            Sorted       : $sorted
            Same         : $same
        """.trimIndent()
    }
}
