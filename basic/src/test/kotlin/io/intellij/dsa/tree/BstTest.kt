package io.intellij.dsa.tree

import io.intellij.dsa.TreeBuilder
import io.intellij.dsa.tree.bst.AVLTree
import io.intellij.dsa.tree.bst.BST
import io.intellij.dsa.tree.bst.printBST
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

/**
 * BstTest
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
class BstTest {

    @Test
    fun `test basic bst`() {
        val basic = TreeBuilder.buildBasicBST<Int, Int>()
        for (i in 1..7) {
            basic.insert(i, i * 10)
        }
        Assertions.assertEquals(7, basic.size())

        for (i in 1..7) {
            basic.remove(i)
        }
        Assertions.assertEquals(0, basic.size())
    }

    @Test
    fun `test avl tree`() {
        val avl = TreeBuilder.buildAVLTree<Int, Int>()
        for (i in 1..7) {
            avl.insert(i, i * 10)
            printBST(avl.getRoot())
            println("----------")
        }

        Assertions.assertEquals(7, avl.size())
        for (i in 1..7) {
            avl.remove(i)
        }
        Assertions.assertEquals(0, avl.size())
    }

    @Test
    fun `test avl ll`() {
        val avl: BST<Int, Int> = AVLTree()
        arrayOf(3, 2, 1).forEach {
            avl.insert(it, it * 10)
            printBST(avl.getRoot())
            println("----------")
        }
    }

    @Test
    fun `test avl lr`() {
        val avl: BST<Int, Int> = AVLTree()
        arrayOf(3, 1, 2).forEach {
            avl.insert(it, it * 10)
            printBST(avl.getRoot())
            println("----------")
        }
    }

    @Test
    fun `test avl rr`() {
        val avl: BST<Int, Int> = AVLTree()
        arrayOf(1, 2, 3).forEach {
            avl.insert(it, it * 10)
            printBST(avl.getRoot())
            println("----------")
        }
    }

    @Test
    fun `test avl rl`() {
        val avl: BST<Int, Int> = AVLTree()
        arrayOf(1, 3, 2).forEach {
            avl.insert(it, it * 10)
            printBST(avl.getRoot())
            println("----------")
        }
    }

}