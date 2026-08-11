package io.intellij.dsa.tree

import io.intellij.dsa.tree.huffman.buildHuffmanTree
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * HuffmanTest
 *
 * @author dev@intellij.io
 */
class HuffmanTest {

  @Test
  fun `huffman tree represents every character with a prefix free code`() {
    val msg = "abbcccddddeeeeeffffff"
    val counts: Map<Char, Int> = msg.groupingBy { it }.eachCount()
    val tree = buildHuffmanTree(counts)

    tree.printTree()
    tree.printEncodingTable()

    val encodingTable = tree.getEncodingTable()
    val codes = encodingTable.values.toList()

    assertEquals(counts.size, tree.size())
    assertEquals(counts.keys, encodingTable.keys)
    assertEquals(msg.length, assertNotNull(tree.getRoot()).weight)
    assertTrue(codes.indices.all { i ->
      codes.indices.none { j -> i != j && codes[j].startsWith(codes[i]) }
    })
  }

}
