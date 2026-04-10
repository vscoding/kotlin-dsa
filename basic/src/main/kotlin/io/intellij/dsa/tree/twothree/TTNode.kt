package io.intellij.dsa.tree.twothree

/**
 * TTNode
 *
 * @author tech@intellij.io
 * @since 2025-06-03
 */
class TTNode<K : Comparable<K>, V> {
  var parent: TTNode<K, V>? = null

  val keys = mutableListOf<KVPair<K, V>>()
  val children = mutableListOf<TTNode<K, V>>()

  fun isLeaf(): Boolean = children.isEmpty()

  fun isTwoNode(): Boolean = keys.size == 1

  fun isThreeNode(): Boolean = keys.size == 2

}

data class KVPair<K, V>(val key: K, val value: V) {
  override fun toString(): String = "($key, $value)"
}
