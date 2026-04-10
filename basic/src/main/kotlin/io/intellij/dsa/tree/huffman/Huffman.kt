package io.intellij.dsa.tree.huffman

/**
 * Huffman 哈夫曼树接口
 *
 * @author tech@intellij.io
 */
interface HuffmanTree {

  fun isEmpty(): Boolean = size() == 0

  fun size(): Int

  /**
   * 获取根节点
   */
  fun getRoot(): HNode?

  /**
   * weighted path length
   */
  fun wpl(): Int

  /**
   * 获取编码表
   */
  fun getEncodingTable(): Map<Char, String>


  fun printEncodingTable()

  fun printTree()
}

/**
 * 哈夫曼树的节点
 */
data class HNode(
  val data: Char,
  val weight: Int,
  val depth: Int = 0,
  val left: HNode? = null,
  val right: HNode? = null,
) {
  fun isLeaf(): Boolean = left == null && right == null
}

internal fun HNode.merge(node: HNode): HNode {
  return HNode(
    data = '?',
    weight = this.weight + node.weight,
    left = if (this.weight <= node.weight) this else node,
    right = if (this.weight > node.weight) this else node,
  )
}

fun buildHuffmanTree(charCountMap: Map<Char, Int>): HuffmanTree {
  return HuffmanTreeImpl(charCountMap)
}
