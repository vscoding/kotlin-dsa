package io.intellij.dsa.tree

import io.intellij.dsa.TreeBuilder
import io.intellij.dsa.tree.twothree.other.TwoThreeTree
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

/**
 * TwoThreeTreeTest
 *
 * @author tech@intellij.io
 * @since 2025-06-02
 */
class TwoThreeTest {

    @Test
    fun `test two three tree inorder`() {
        TwoThreeTree().apply {
            // 10, 20, 5, 15, 30, 25, 35
            insert(10)
            insert(20)
            insert(5)
            insert(15)
            insert(30)
            insert(25)
            insert(35)
        }.inorder()
    }

    @Test
    fun `test two tree tree insert`() {
        val tree = TreeBuilder.buildTTTree<Int, String>().apply {
            for (i in 1..9) {
                this.insert(i, "value-$i")
            }
        }
        Assertions.assertEquals(9, tree.size())

        println("Inorder Traversal:")
        tree.inorder { key, value -> println("($key, $value)") }

    }

}