package io.intellij.dsa.uf

import java.util.TreeMap

/**
 * TreeIdUnionFind 连接对象的唯一标识符
 *
 * @author tech@intellij.io
 * @since 2025-06-01
 */
class TreeIdUnionFind<T, ID : Comparable<ID>>(
  private val identifierFunction: (T) -> ID,
) : UnionFind<T> {

  private val storage: TreeMap<ID, Node> = TreeMap()

  override fun contains(value: T): Boolean {
    return doContains(value)
  }

  private fun doContains(data: T): Boolean {
    val id = getIdentifier(data) ?: return false
    return storage.containsKey(id)
  }

  override fun size(): Int {
    return storage.size
  }

  override fun add(value: T): Boolean {
    val identifier = getIdentifier(value) ?: return false
    return doAdd(Node(identifier), check = true) != null
  }

  private fun doAdd(addNode: Node, check: Boolean): Node? {
    val id = addNode.id
    return if (storage.containsKey(id)) {
      if (check) {
        null
      } else {
        storage[id]
      }
    } else {
      storage[id] = addNode
      addNode
    }
  }

  override fun union(value1: T, value2: T): Boolean {
    val xNode = doAdd(Node(getIdentifier(value1) ?: return false), check = false)
    val yNode = doAdd(Node(getIdentifier(value2) ?: return false), check = false)

    if (xNode == null || yNode == null) {
      return false
    }

    union(xNode, yNode)
    return true
  }

  private fun union(src: Node, cur: Node) {
    val srcParent = getParent(src)
    val curParent = getParent(cur)

    // 已经连接
    if (idEquals(srcParent.id, curParent.id)) {
      return
    }

    // 树压缩
    curParent.parent = srcParent
    srcParent.children.add(curParent)

    srcParent.children.addAll(curParent.children)
    curParent.children.forEach { child ->
      child.parent = srcParent
    }
    curParent.children.clear()
  }

  private fun getParent(node: Node): Node {
    return node.parent?.let { getParent(it) } ?: node
  }

  override fun isConnected(value1: T, value2: T): Boolean {
    if (!contains(value1) || !contains(value2)) {
      return false
    }

    val id1 = getIdentifier(value1)!!
    val id2 = getIdentifier(value2)!!
    val node1 = storage[id1]!!
    val node2 = storage[id2]!!

    return idEquals(getParent(node1).id, getParent(node2).id)
  }

  private fun getIdentifier(data: T): ID? {
    return identifierFunction(data)
  }

  // 先比较 equals 再比较 compareTo
  private fun idEquals(id1: ID, id2: ID): Boolean {
    return id1 == id2 || id1.compareTo(id2) == 0
  }

  override fun clear() {
    storage.clear()
  }

  /**
   * 内部节点类
   */
  private inner class Node(val id: ID) {
    var parent: Node? = null
    val children: MutableList<Node> = mutableListOf()
  }

}
