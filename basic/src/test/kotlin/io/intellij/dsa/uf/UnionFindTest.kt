package io.intellij.dsa.uf

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * UnionFindTest
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class UnionFindTest {
  val a = Node(1, "a")
  val b = Node(2, "b")
  val c = Node(3, "c")
  val d = Node(4, "d")
  val e = Node(5, "e")
  val f = Node(6, "f")

  @Test
  fun `test indexed union find`() {
    val uf: UnionFind<Node> = IndexedUnionFind(Node::id)

    uf.union(a, b)
    uf.union(b, c)

    uf.union(d, e)
    uf.union(e, f)

    Assertions.assertTrue(uf.isConnected(a, c))
    Assertions.assertFalse(uf.isConnected(a, d))

    uf.union(a, f)
    Assertions.assertTrue(uf.isConnected(c, d))

  }

  @Test
  fun `test tree like union find`() {
    val uf: UnionFind<Node> = TreeUnionFind(Comparator.comparingInt(Node::id))

    uf.union(a, b)
    uf.union(b, c)

    uf.union(d, e)
    uf.union(e, f)

    Assertions.assertTrue(uf.isConnected(a, c))
    Assertions.assertFalse(uf.isConnected(a, d))

    uf.union(a, f)
    Assertions.assertTrue(uf.isConnected(c, d))

  }

  @Test
  fun `test tree like union find by id`() {
    val uf: UnionFind<Node> = TreeIdUnionFind({ it.id })

    uf.union(a, b)
    uf.union(b, c)

    uf.union(d, e)
    uf.union(e, f)

    Assertions.assertTrue(uf.isConnected(a, c))
    Assertions.assertFalse(uf.isConnected(a, d))

    uf.union(a, f)
    Assertions.assertTrue(uf.isConnected(c, d))

  }

  data class Node(val id: Int, val name: String)

}