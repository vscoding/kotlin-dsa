package io.intellij.dsa.tree

import io.intellij.dsa.tree.huffman.buildHuffmanTree
import org.junit.jupiter.api.Test

/**
 * HuffmanTest
 *
 * @author tech@intellij.io
 */
class HuffmanTest {

  @Test
  fun `test build huffman tree`() {
    val msg = "abbcccddddeeeeeffffff"
    // msg 转成 char的计数
    val counts: Map<Char, Int> = msg.groupingBy { it }.eachCount()

    buildHuffmanTree(counts).let {
      it.printTree()
      it.printEncodingTable()
    }
  }

}
