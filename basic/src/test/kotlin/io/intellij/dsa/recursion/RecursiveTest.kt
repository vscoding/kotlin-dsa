package io.intellij.dsa.recursion

import org.junit.jupiter.api.Test
import java.util.ArrayDeque

/**
 * RecursiveTest
 *
 * @author tech@intellij.io
 */
class RecursiveTest {

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
  fun `test fibonacci`() {
    println(fibonacciW(50))

    println(fibonacciW(100))
  }

  data class Node(val value: String, val children: List<Node>)

  private fun traversalR(node: Node, depth: Int = 0) {
    println("  ".repeat(depth) + node.value)
    node.children.forEach { child ->
      traversalR(child, depth + 1)
    }
  }

  private fun traversalW(node: Node) {
    val queue = ArrayDeque<Node>().apply { add(node) }
    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()
      println(current.value)
      queue.addAll(current.children)
    }
  }

  @Test
  fun `test tree`() {
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
    traversalW(root)
  }
}
