package io.intellij.dsa.tree.trie

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * TrieImpl
 *
 * @author tech@intellij.io
 */
class StringTrie : Trie {

  private class ChildrenNode(
    val children: MutableMap<String, ChildrenNode> = java.util.HashMap(4),
    // 标记到这这里是否构成一个完整的内容
    var terminal: Boolean = false,
  ) {
    fun isLeaf(): Boolean = children.isEmpty()
    fun getChild(fragment: String): ChildrenNode? = children[fragment]
  }

  // 用单一哨兵根节点替代多张根层 Map
  private val root: ChildrenNode = ChildrenNode()

  private var contentCount: Int = 0

  /**
   * 片段分割的方法
   */
  private val segmentFunc: (String) -> List<String>

  constructor(segmentFunc: (String) -> List<String>) {
    this.segmentFunc = segmentFunc
  }

  override fun size(): Int = contentCount

  override fun addContent(content: String) {
    val fragments = segmentFunc(content)
    // 支持空串：直接在根节点标记 terminal
    if (fragments.isEmpty()) {
      if (!root.terminal) {
        root.terminal = true
        contentCount++
      }
      return
    }

    var node = root
    for (fragment in fragments) {
      node = node.children.getOrPut(fragment) { ChildrenNode() }
    }
    if (!node.terminal) {
      node.terminal = true
      contentCount++
    }

  }

  override fun contains(content: String): Boolean {
    val fragments = segmentFunc(content)
    if (fragments.isEmpty()) {
      return root.terminal
    }

    var node: ChildrenNode = root
    for (fragment in fragments) {
      node = node.getChild(fragment) ?: return false
    }
    return node.terminal
  }

  override fun containsPartial(content: String): Boolean {
    val fragments = segmentFunc(content)
    return if (fragments.isEmpty()) {
      false
    } else {
      var node: ChildrenNode = root
      for (fragment in fragments) {
        node = node.getChild(fragment) ?: return false
        if (node.terminal) {
          return true
        }
      }
      false
    }

  }

  override fun getSegmentFunc(): (String) -> List<String> = this.segmentFunc

  override fun clear() {
    root.children.clear()
    root.terminal = false
    contentCount = 0
  }
}

class TrieUtils {
  companion object {
    /**
     * 一些片段分割方法的模板
     */
    fun segmentFunTpl(tplName: String): (String) -> List<String> {

      when (tplName) {
        "split" -> return { content ->
          content.split(" ")
        }

        "word" -> return { content ->
          content.split("(?!^)".toRegex())
        }

        "domain" -> return { domainName ->
          domainName.split(".").reversed()
        }
      }

      throw IllegalArgumentException("Unknown segment function template: $tplName")
    }


    /**
     * trie
     */
    fun buildTrieFromTxtFile(
      input: InputStream,
      segmentFunc: (String) -> List<String>,
    ): Trie {
      val trie = StringTrie(segmentFunc)
      BufferedReader(InputStreamReader(input)).useLines { lines ->
        lines.forEach { line ->
          if (line.isNotBlank()) {
            trie.addContent(line)
          }
        }
      }
      return trie
    }
  }
}