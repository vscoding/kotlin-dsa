package io.intellij.dsa.tree.huffman

import io.intellij.dsa.tree.heap.Heap
import io.intellij.dsa.tree.heap.HeapImpl
import io.intellij.dsa.tree.heap.HeapType

/**
 * 哈夫曼树的实现
 */
internal class HuffmanTreeImpl : HuffmanTree {
  private var root: HNode? = null
  private var size: Int = 0
  private val encodingTable: MutableMap<Char, String> = mutableMapOf()

  constructor(charCountMap: Map<Char, Int>) {
    this.root = this.buildTree(charCountMap)
    this.buildEncodingTable(this.root)
  }

  override fun size() = this.size

  /**
   * 不针对 size=0 或者 size = 1 的进行构建
   */
  private fun buildTree(charCountMap: Map<Char, Int>): HNode? {
    if (charCountMap.size <= 1) {
      println("Huffman Tree cannot be built with less than 2 characters.")
      return null
    }

    this.size = charCountMap.size

    val heap: Heap<HNode> = HeapImpl(HeapType.MIN_HEAP, Comparator.comparing { it.weight })
    charCountMap.forEach { (c, count) ->
      heap.add(HNode(data = c, weight = count))
    }

    while (heap.size() > 1) {
      val min = heap.extract()
      val secondMin = heap.extract()
      heap.add(min!!.merge(secondMin!!))
    }
    return heap.extract()
  }

  override fun getRoot(): HNode? = root

  override fun wpl(): Int {
    return 0
  }

  override fun getEncodingTable(): Map<Char, String> = encodingTable

  /**
   * 构建编码表
   *
   */
  private fun buildEncodingTable(root: HNode?) {
    if (root == null) {
      return
    }
    this.dfsBuildEncodingTable(root, "0")
  }

  /**
   * 向左为0 向右为1
   */
  private fun dfsBuildEncodingTable(node: HNode, current: String) {
    if (node.isLeaf()) {
      encodingTable[node.data] = current
    }
    node.left?.let { dfsBuildEncodingTable(it, current + "0") }
    node.right?.let { dfsBuildEncodingTable(it, current + "1") }
  }

  override fun printEncodingTable() {
    println("Encoding Table:")
    encodingTable.forEach { (c, s) ->
      println("'$c':$s")
    }
  }

  override fun printTree() {
    val r = this.root
    if (r == null) {
      println("Empty Tree")
      return
    }
    // 根节点不加连接符
    println(nodeLabel(r))
    // 递归打印左右子树
    printTree(r.left, prefix = "", isLeft = true)
    printTree(r.right, prefix = "", isLeft = false)
  }

  private fun printTree(node: HNode?, prefix: String, isLeft: Boolean) {
    if (node == null) return
    val branch = if (isLeft) "├── " else "└── "
    println(prefix + branch + nodeLabel(node))
    val childPrefix = prefix + if (isLeft) "│   " else "    "
    printTree(node.left, childPrefix, true)
    printTree(node.right, childPrefix, false)
  }

  private fun nodeLabel(n: HNode): String {
    return if (n.isLeaf()) "'${n.data}':${n.weight}" else "*:${n.weight}"
  }

}
