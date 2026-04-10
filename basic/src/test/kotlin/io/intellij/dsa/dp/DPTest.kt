package io.intellij.dsa.dp

import org.junit.jupiter.api.Test

/**
 * DPTest
 *
 * @author tech@intellij.io
 * @since 2025-06-03
 */
class DPTest {

  @Test
  fun `test number triangles`() {

    NumberTriangles().apply {
      val triangles = """
                5
                7
                3 8
                8 1 0
                2 7 4 4
                4 5 2 6 5
            """.trimIndent()
      val result = solution(triangles)
      println("Maximum path sum: $result") // 应该输出最大路径和
    }

  }

}