package io.intellij.dsa.uf

import java.util.TreeMap

/**
 * TreeUnionFind 连接对象本身
 *
 * @author tech@intellij.io
 * @since 2025-06-01
 */
class TreeUnionFind<T> : UnionFind<T> {

  private val comparator: Comparator<T>
  private val storage: TreeMap<T, Node<T>>

  constructor(comparator: Comparator<T>) {
    this.comparator = comparator
    this.storage = TreeMap(comparator)
  }

  override fun size(): Int = this.storage.size

  override fun contains(value: T): Boolean {
    return this.storage.containsKey(value)
  }

  override fun add(value: T): Boolean {
    if (this.storage.containsKey(value)) {
      return false
    }
    doAdd(value)
    return true
  }

  private fun doAdd(value: T): Node<T> {
    val node = storage[value]
    return if (node == null) {
      storage[value] = Node(value)
      storage[value]!!
    } else {
      node
    }
  }

  override fun union(value1: T, value2: T): Boolean {
    val node1 = doAdd(value1)
    val node2 = doAdd(value2)

    if (comparator.compare(node1.data, node2.data) == 0) {
      return false
    }

    val parent1 = getParent(node1)
    val parent2 = getParent(node2)

    if (comparator.compare(parent1.data, parent2.data) == 0) {
      return true
    }

    parent1.children.addAll(
      parent2.children.apply {
        forEach { it.parent = parent1 }
      },
    )
    parent2.children.clear()

    parent2.parent = parent1
    parent1.children.add(parent2)

    return true
  }

  private fun getParent(node: Node<T>): Node<T> {
    var current = node
    while (current.parent != null) {
      current = current.parent!!
    }
    return current
  }

  override fun isConnected(value1: T, value2: T): Boolean {
    val node1 = storage[value1] ?: return false
    val node2 = storage[value2] ?: return false

    val parent1 = getParent(node1)
    val parent2 = getParent(node2)

    return comparator.compare(parent1.data, parent2.data) == 0
  }

  override fun clear() {
    this.storage.clear()
  }

  inner class Node<T> {
    var parent: Node<T>? = null

    val data: T
    val children: MutableList<Node<T>> = mutableListOf()

    constructor(data: T) {
      this.data = data
    }
  }

}