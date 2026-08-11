package io.intellij.dsa.uf

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * UnionFindTest
 *
 * @author dev@intellij.io
 * @since 2025-05-31
 */
class UnionFindTest {
  private val a = Node(1, "a")
  private val b = Node(2, "b")
  private val c = Node(3, "c")
  private val d = Node(4, "d")
  private val e = Node(5, "e")
  private val f = Node(6, "f")

  @Test
  fun `indexed union find connects disjoint groups`() {
    assertUnionFind(IndexedUnionFind(Node::id))
  }

  @Test
  fun `tree union find connects disjoint groups`() {
    assertUnionFind(TreeUnionFind(Comparator.comparingInt(Node::id)))
  }

  @Test
  fun `tree id union find connects disjoint groups`() {
    assertUnionFind(TreeIdUnionFind(Node::id))
  }

  private fun assertUnionFind(uf: UnionFind<Node>) {
    uf.union(a, b)
    uf.union(b, c)
    uf.union(d, e)
    uf.union(e, f)

    assertEquals(6, uf.size())
    assertTrue(uf.isConnected(a, c))
    assertFalse(uf.isConnected(a, d))

    uf.union(a, f)
    assertTrue(uf.isConnected(c, d))

    uf.clear()
    assertTrue(uf.isEmpty())
  }

  data class Node(val id: Int, val name: String)

}
