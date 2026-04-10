package io.intellij.dsa.tree.bst

/**
 * BSTNode
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
interface BSTNode<K : Comparable<K>, V> {

  /**
   * 节点高度，定义为节点到根节点的高度
   */
  fun getHeight(): Int

  fun setHeight(height: Int): BSTNode<K, V>

  fun getKey(): K

  fun setKey(key: K): BSTNode<K, V>

  fun getValue(): V

  fun setValue(value: V): BSTNode<K, V>

  fun getLeft(): BSTNode<K, V>?

  fun setLeft(left: BSTNode<K, V>?): BSTNode<K, V>

  fun getRight(): BSTNode<K, V>?

  fun setRight(right: BSTNode<K, V>?): BSTNode<K, V>

  fun isLeaf(): Boolean {
    return this.getLeft() == null && this.getRight() == null
  }
}
