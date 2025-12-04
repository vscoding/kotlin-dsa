package io.intellij.dsa.tree.twothree

import io.intellij.dsa.DataStructurePrintable
import io.intellij.dsa.KVOperator

/**
 * TTTree 二三树，自下而上的平衡
 *
 * @author tech@intellij.io
 * @since 2025-06-03
 */
interface TTTree<K : Comparable<K>, V> : KVOperator<K, V>, DataStructurePrintable {

    fun getRoot(): TTNode<K, V>?

    /**
     * 中序遍历
     */
    fun inorder(action: (K, V) -> Unit)

}
