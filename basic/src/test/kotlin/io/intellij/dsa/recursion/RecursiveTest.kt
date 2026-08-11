package io.intellij.dsa.recursion

import io.intellij.dsa.getLogger
import org.junit.jupiter.api.Test
import java.util.ArrayDeque
import kotlin.test.assertEquals

/**
 * RecursiveTest
 *
 * @author dev@intellij.io
 */
class RecursiveTest {

  companion object {
    private val log = getLogger(RecursiveTest::class.java)
  }

  private fun fibonacciR(n: Int): Long {
    return if (n == 1 || n == 2) {
      1
    } else {
      fibonacciR(n - 1) + fibonacciR(n - 2)
    }
  }

  private fun fibonacciW(n: Int): Long {
    if (n <= 2) return 1L
    var prev = 1L
    var curr = 1L

    repeat(n - 2) {
      // 使用解构赋值
      prev = curr.also { curr += prev }
    }
    return curr
  }

  @Test
  fun `recursive and iterative fibonacci produce the same value`() {
    val recursive = fibonacciR(10)
    val iterative = fibonacciW(10)

    log.info("fibonacciR(10) = $recursive")
    log.info("fibonacciW(10) = $iterative")
    log.info("${fibonacciW(50)}")
    log.info("${fibonacciW(100)}")

    assertEquals(55, recursive)
    assertEquals(recursive, iterative)
  }

  data class Node(val value: String, val children: List<Node>)

  private fun traversalR(node: Node, depth: Int = 0) {
    log.info("  ".repeat(depth) + node.value)
    node.children.forEach { child ->
      traversalR(child, depth + 1)
    }
  }

  private fun traversalW(node: Node): List<String> {
    val visited = mutableListOf<String>()
    val queue = ArrayDeque<Node>().apply { add(node) }
    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()
      visited.add(current.value)
      log.info(current.value)
      queue.addAll(current.children)
    }
    return visited
  }

  @Test
  fun `iterative tree traversal visits nodes breadth first`() {
    val root = Node(
      "A",
      listOf(
        Node(
          "B",
          listOf(
            Node("D", emptyList()),
            Node("E", emptyList()),
          ),
        ),
        Node(
          "C",
          listOf(
            Node("F", emptyList()),
            Node("G", emptyList()),
          ),
        ),
      ),
    )
    assertEquals(listOf("A", "B", "C", "D", "E", "F", "G"), traversalW(root))
  }
}
