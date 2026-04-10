package io.intellij.dsa.tree.twothree.other

/**
 * TwoThreeTree
 *
 * @author tech@intellij.io
 * @since 2025-06-02
 */
class TwoThreeTree {
  private inner class Node {
    val keys = mutableListOf<Int>()
    val children = mutableListOf<Node>()
    var parent: Node? = null

    fun isLeaf(): Boolean = children.isEmpty()

    fun isTwoNode(): Boolean = keys.size == 1

    fun isThreeNode(): Boolean = keys.size == 2

    fun insertKey(key: Int) {
      when {
        keys.isEmpty() || key < keys[0] -> keys.add(0, key)
        keys.size == 1 || key < keys[1] -> keys.add(1, key)
        else -> keys.add(2, key)
      }
    }
  }

  private var root: Node? = null

  fun insert(key: Int) {
    if (root == null) {
      root = Node().apply { keys.add(key) }
      return
    }

    val leaf = findLeaf(root!!, key)
    insertAndSplit(leaf, key)
  }

  private fun findLeaf(node: Node, key: Int): Node {
    var current = node
    while (!current.isLeaf()) {
      current = when {
        key < current.keys[0] -> current.children[0]
        current.isTwoNode() || key < current.keys[1] -> current.children[1]
        else -> current.children[2]
      }
    }
    return current
  }

  private fun insertAndSplit(node: Node, key: Int) {
    node.insertKey(key)

    if (node.keys.size <= 2) return

    split(node)
  }

  private fun split(node: Node) {
    val middle = node.keys[1]
    val left = Node()
    val right = Node()

    left.keys.add(node.keys[0])
    right.keys.add(node.keys[2])

    if (!node.isLeaf()) {
      left.children.addAll(listOf(node.children[0], node.children[1]))
      right.children.addAll(listOf(node.children[2], node.children[3]))
      left.children.forEach { it.parent = left }
      right.children.forEach { it.parent = right }
    }

    val parent = node.parent
    if (parent == null) {
      root = Node().apply {
        keys.add(middle)
        children.addAll(listOf(left, right))
      }
      left.parent = root
      right.parent = root
    } else {
      val index = parent.children.indexOf(node)
      parent.children.removeAt(index)
      parent.children.add(index, right)
      parent.children.add(index, left)
      left.parent = parent
      right.parent = parent
      parent.insertKey(middle)

      if (parent.keys.size > 2) {
        split(parent)
      }
    }
  }

  fun inorder() {
    inorder(root)
    println()
  }

  private fun inorder(node: Node?) {
    if (node == null) return

    if (node.isLeaf()) {
      node.keys.forEach { print("$it ") }
      return
    }

    if (node.isTwoNode()) {
      inorder(node.children[0])
      print("${node.keys[0]} ")
      inorder(node.children[1])
    } else {
      inorder(node.children[0])
      print("${node.keys[0]} ")
      inorder(node.children[1])
      print("${node.keys[1]} ")
      inorder(node.children[2])
    }
  }
}