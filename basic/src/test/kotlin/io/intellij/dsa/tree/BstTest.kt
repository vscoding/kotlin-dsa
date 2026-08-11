package io.intellij.dsa.tree

import io.intellij.dsa.tree.bst.AVLTree
import io.intellij.dsa.tree.bst.BST
import io.intellij.dsa.tree.bst.printBST
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * BstTest
 *
 * @author dev@intellij.io
 * @since 2025-05-30
 */
class BstTest {

  @Test
  fun `basic bst stores and removes all entries`() {
    val basic = TreeBuilder.buildBasicBST<Int, Int>()
    for (i in 1..7) {
      basic.insert(i, i * 10)
    }

    assertEquals(7, basic.size())
    assertTrue(basic.isBst())
    assertEquals(1, basic.getMin()?.getKey())
    for (i in 1..7) {
      assertEquals(i * 10, basic.get(i))
    }

    for (i in 1..7) {
      assertEquals(i * 10, basic.remove(i))
    }
    assertTrue(basic.isEmpty())
  }

  @Test
  fun `avl tree remains a bst while entries are inserted and removed`() {
    val avl = TreeBuilder.buildAVLTree<Int, Int>()
    for (i in 1..7) {
      avl.insert(i, i * 10)
      printBST(avl.getRoot())
      println("----------")
    }

    assertEquals(7, avl.size())
    assertTrue(avl.isBst())
    assertEquals(1, avl.getMin()?.getKey())

    for (i in 1..7) {
      assertEquals(i * 10, avl.remove(i))
    }
    assertTrue(avl.isEmpty())
  }

  @Test
  fun `avl performs ll rotation`() {
    assertRotation(arrayOf(3, 2, 1))
  }

  @Test
  fun `avl performs lr rotation`() {
    assertRotation(arrayOf(3, 1, 2))
  }

  @Test
  fun `avl performs rr rotation`() {
    assertRotation(arrayOf(1, 2, 3))
  }

  @Test
  fun `avl performs rl rotation`() {
    assertRotation(arrayOf(1, 3, 2))
  }

  private fun assertRotation(values: Array<Int>) {
    val avl: BST<Int, Int> = AVLTree()
    values.forEach {
      avl.insert(it, it * 10)
      printBST(avl.getRoot())
      println("----------")
    }

    assertEquals(2, avl.getRoot()?.getKey())
    assertTrue(avl.isBst())
  }

}
