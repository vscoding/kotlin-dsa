package io.intellij.dsa.dp

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * DPTest
 *
 * @author dev@intellij.io
 * @since 2025-06-03
 */
class DPTest {

  @Test
  fun `number triangle returns the maximum path sum`() {
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
      println("Maximum path sum: $result")
      assertEquals(30, result)
    }
  }

}
