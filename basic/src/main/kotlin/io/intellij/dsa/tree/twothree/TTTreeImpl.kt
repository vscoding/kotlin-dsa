package io.intellij.dsa.tree.twothree

import io.intellij.dsa.getLogger

/**
 * TTTreeImpl
 *
 * @author tech@intellij.io
 * @since 2025-06-03
 */
class TTTreeImpl<K : Comparable<K>, V> : TTTree<K, V> {

  companion object {
    val log = getLogger(TTTreeImpl::class.java)
  }

  private var root: TTNode<K, V>? = null
  private var count = 0

  override fun getRoot(): TTNode<K, V>? = this.root

  override fun size(): Int = this.count

  override fun contains(key: K): Boolean = get(key) != null

  override fun get(key: K): V? {
    return get(this.root, key)
  }

  private fun get(node: TTNode<K, V>?, key: K): V? {
    if (node == null) return null

    if (node.isTwoNode()) {
      return when {
        key < node.keys[0].key -> get(node.children[0], key)
        key == node.keys[0].key -> node.keys[0].value
        else -> get(node.children[1], key)
      }
    } else if (node.isThreeNode()) {
      return when {
        key < node.keys[0].key -> get(node.children[0], key)
        key == node.keys[0].key -> node.keys[0].value
        key < node.keys[1].key -> get(node.children[1], key)
        key == node.keys[1].key -> node.keys[1].value
        else -> get(node.children[2], key)
      }
    } else {
      throw IllegalStateException("Invalid Node")
    }
  }

  override fun insert(key: K, value: V) {
    if (this.root == null) {
      this.root = TTNode<K, V>().apply { add(key, value) }
      this.count++
      return
    }
    findLeaf(this.root!!, key, value)?.apply {
      // 不为空，说明没有替换值，添加新键值对
      this.add(key, value)
      count++
      // 如果当前节点的键值对数量超过2，进行分裂
      if (this.keys.size > 2) {
        splitAndRebalance(this)
      }
    }

  }

  private fun TTNode<K, V>.add(key: K, value: V) {
    when {
      keys.isEmpty() || key < keys[0].key -> keys.add(0, KVPair(key, value))
      keys.size == 1 || key < keys[1].key -> keys.add(1, KVPair(key, value))
      else -> keys.add(2, KVPair(key, value))
    }
  }

  // 如果在查找叶子节点的过程中发现值相同，替换值返回null
  private fun findLeaf(node: TTNode<K, V>, key: K, value: V): TTNode<K, V>? {
    var current = node
    while (!current.isLeaf()) {
      when {
        current.isTwoNode() ->
          when {
            key < current.keys[0].key -> current = current.children[0]
            key == current.keys[0].key -> {
              current.keys[0] = KVPair(key, value)
              return null // 替换值返回null
            }

            else -> current = current.children[1]
          }

        current.isThreeNode() ->
          when {
            key < current.keys[0].key -> current = current.children[0]
            key == current.keys[0].key -> {
              current.keys[0] = KVPair(key, value)
              return null // 替换值返回null
            }

            key < current.keys[1].key -> current = current.children[1]
            key == current.keys[1].key -> {
              current.keys[1] = KVPair(key, value)
              return null // 替换值返回null
            }

            else -> current = current.children[2]
          }

        else -> throw IllegalStateException("Invalid Node")
      }
    }
    return current
  }

  // 一定是3节点分裂
  private fun splitAndRebalance(node: TTNode<K, V>) {
    log.debug("split and rebalance node: [${node.keys.map { it -> it.key }.joinToString(" ")}]")
    /*
    case1:

        x y z     y
                /  \
               x    z

    case2:
          x  y  z             y
        /   |  \  \         /   \
      a    b   c   d       x      z
                         /  \    /  \
                        a    b  c    d
     */

    val midKV = node.keys[1]

    // 创建两个新节点
    val leftNode = TTNode<K, V>()
    val rightNode = TTNode<K, V>()

    leftNode.add(node.keys[0].key, node.keys[0].value)
    rightNode.add(node.keys[2].key, node.keys[2].value)

    // 向上递归会遇到叶子节点
    if (!node.isLeaf()) {
      // 子节点重新分配
      // 左子节点包含原节点的前两个子节点
      // 右子节点包含原节点的后两个子节点

      leftNode.children.addAll(listOf(node.children[0], node.children[1]))
      rightNode.children.addAll(listOf(node.children[2], node.children[3]))

      // 设置子节点的父节点
      leftNode.children.forEach { it.parent = leftNode }
      rightNode.children.forEach { it.parent = rightNode }
    }

    val curParent = node.parent
    if (curParent == null) {
      this.root = TTNode<K, V>().apply {
        this.add(midKV.key, midKV.value)
        this.children.add(leftNode)
        this.children.add(rightNode)
      }
      // parent指向的形成
      leftNode.parent = this.root
      rightNode.parent = this.root
    } else {

      val index = curParent.children.indexOf(node)
      curParent.children.removeAt(index)

      // 这里parent可能会形成4节点
      curParent.children.add(index, rightNode)
      curParent.children.add(index, leftNode)

      leftNode.parent = curParent
      rightNode.parent = curParent

      curParent.add(midKV.key, midKV.value)
      if (curParent.keys.size > 2) {
        splitAndRebalance(curParent)
      }
    }

  }

  override fun remove(key: K): V? {
    TODO("Not yet implemented")
  }

  override fun inorder(action: (K, V) -> Unit) {
    inorder(this.root, action)
  }

  private fun inorder(node: TTNode<K, V>?, action: (K, V) -> Unit) {
    if (node == null) return

    when {
      node.isLeaf() -> node.keys.forEach { action(it.key, it.value) }

      node.isTwoNode() -> {

        val left = node.children[0]
        val right = node.children[1]

        inorder(left, action)
        node.keys.forEach { action(it.key, it.value) }
        inorder(right, action)

      }

      node.isThreeNode() -> {

        val left = node.children[0]
        val middle = node.children[1]
        val right = node.children[2]

        inorder(left, action)
        action(node.keys[0].key, node.keys[0].value)
        inorder(middle, action)
        action(node.keys[1].key, node.keys[1].value)
        inorder(right, action)
      }

      else -> throw IllegalStateException("Invalid Node")
    }
  }

  override fun clear() {
    this.root == null
    this.count = 0
  }

  override fun print() {
    if (this.root == null) return println("Empty Tree")

    println("2-3 Tree Structure:")
    printTreeHelper(root, "", true)
  }

  private fun printTreeHelper(node: TTNode<K, V>?, prefix: String, isLast: Boolean) {
    if (node == null) return

    // 打印当前节点
    val connector = if (isLast) "└── " else "├── "
    val nodeContent = if (node.keys.isEmpty()) "[]" else node.keys.joinToString(", ") { "${it.key}" }
    println("$prefix$connector$nodeContent")

    // 如果不是叶子节点，递归打印子节点
    if (!node.isLeaf()) {
      val childPrefix = prefix + if (isLast) "    " else "│   "

      for (i in node.children.indices) {
        val isLastChild = (i == node.children.size - 1)
        printTreeHelper(node.children[i], childPrefix, isLastChild)
      }
    }
  }

}
