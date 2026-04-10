package io.intellij.dsa.dp

import kotlin.math.max

/**
 * NumberTriangles 数字三角形
 *
 * @author tech@intellij.io
 * @since 2025-06-03
 */
class NumberTriangles {

  fun solution(triangles: String): Int {
    /*
        5
        7
        3 8
        8 1 0
        2 7 4 4
        4 5 2 6 5
     */
    val triangle = parseTriangle(triangles)

    if (triangle.isEmpty()) return 0
    if (triangle.size == 1) return triangle[0][0]

    var tmpMax = triangle[0][0]

    val maxContainer: MutableList<MutableList<Int>> = mutableListOf<MutableList<Int>>().apply {
      // 初始化第一行
      this.add(mutableListOf(tmpMax))
    }

    for (i in 1 until triangle.size) {
      maxContainer.add(mutableListOf())
      for (j in 0..i) {
        val value = triangle[i][j]
        when (j) {
          0 -> {
            // 每行第一个,边缘点
            val startMax = value + maxContainer[i - 1][0]
            maxContainer[i].add(startMax)
            tmpMax = startMax
          }

          i -> {
            // 每行最后一个
            val endMax = value + maxContainer[i - 1][i - 1]
            maxContainer[i].add(endMax)
            tmpMax = max(tmpMax, endMax)
          }

          else -> {
            val leftParent = maxContainer[i - 1][j - 1]
            val rightParent = maxContainer[i - 1][j]
            val curMax = value + max(leftParent, rightParent)
            maxContainer[i].add(curMax)
            tmpMax = max(tmpMax, curMax)
          }
        }
      }
    }

    return tmpMax
  }

  fun parseTriangle(triangles: String): List<List<Int>> {
    val lines = triangles.trim().lines()
    if (lines.isEmpty()) return emptyList()

    val size = lines[0].toIntOrNull() ?: return emptyList()
    if (lines.size != size + 1) return emptyList()

    val triangle = mutableListOf<List<Int>>()
    for (i in 1..size) {
      val row = lines[i].trim().split(" ").mapNotNull { it.toIntOrNull() }
      if (row.size != i) return emptyList() // 每行的数字个数必须等于行号
      triangle.add(row)
    }
    return triangle
  }

}