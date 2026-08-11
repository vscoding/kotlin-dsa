package io.intellij.dsa.tree

import io.intellij.dsa.tree.twothree.sample.TwoThreeTree
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * TwoThreeTreeTest
 *
 * @author dev@intellij.io
 * @since 2025-06-02
 */
class TwoThreeTest {

  @Test
  fun `sample two three tree prints its inorder traversal`() {
    assertDoesNotThrow {
      TwoThreeTree().apply {
        listOf(10, 20, 5, 15, 30, 25, 35).forEach(::insert)
      }.inorder()
    }
  }

  @Test
  fun `two three tree traverses inserted entries in key order`() {
    val tree = TreeBuilder.buildTTTree<Int, String>().apply {
      for (i in 1..9) {
        this.insert(i, "value-$i")
      }
    }
    val traversal = mutableListOf<Pair<Int, String>>()

    println("Inorder Traversal:")
    tree.inorder { key, value ->
      traversal.add(key to value)
      println("($key, $value)")
    }

    assertEquals(9, tree.size())
    assertEquals((1..9).map { it to "value-$it" }, traversal)
  }

}
