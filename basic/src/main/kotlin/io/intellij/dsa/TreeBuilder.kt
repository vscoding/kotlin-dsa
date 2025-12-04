package io.intellij.dsa

import io.intellij.dsa.skiplist.DEFAULT_MAX_LEVEL
import io.intellij.dsa.skiplist.DEFAULT_P
import io.intellij.dsa.skiplist.SkipList
import io.intellij.dsa.skiplist.SkipListImpl
import io.intellij.dsa.tree.bst.AVLTree
import io.intellij.dsa.tree.bst.BST
import io.intellij.dsa.tree.bst.BasicBST
import io.intellij.dsa.tree.twothree.TTTree
import io.intellij.dsa.tree.twothree.TTTreeImpl

object TreeBuilder {
    /**
     * Creates a new basic binary search tree (BST).
     */
    fun <K : Comparable<K>, V> buildBasicBST(): BST<K, V> = BasicBST()


    /**
     * Builds an empty AVL tree that implements the Binary Search Tree (BST) interface.
     *
     * AVL trees maintain a balanced structure, ensuring that the height difference between
     * the left and right subtrees of any node is at most one. This guarantees logarithmic
     * time complexity for insertion, deletion, and search operations.
     *
     * @return an instance of an empty AVL tree implementing BST
     */
    fun <K : Comparable<K>, V> buildAVLTree(): BST<K, V> = AVLTree()


    /**
     * Builds and returns a new instance of a 2-3 Tree (TTTree).
     *
     * The resulting TTTree is a balanced binary search tree
     * that maintains data in sorted order and supports key-based
     * value retrieval, insertion, and deletion operations.
     *
     * @return A new instance of TTTree implementation.
     */
    fun <K : Comparable<K>, V> buildTTTree(): TTTree<K, V> = TTTreeImpl()


    /**
     * Builds a SkipList with the specified maximum level and probability factor.
     *
     * @param maxLevel the maximum number of levels in the SkipList. Defaults to DEFAULT_MAX_LEVEL.
     * @param p the probability factor that determines level assignment. Defaults to DEFAULT_P.
     * @return a new instance of SkipList initialized with the given parameters.
     */
    fun <K : Comparable<K>, V> buildSkipList(maxLevel: Int = DEFAULT_MAX_LEVEL, p: Double = DEFAULT_P): SkipList<K, V> {
        return SkipListImpl(maxLevel, p)
    }

}