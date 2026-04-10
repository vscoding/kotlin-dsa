package io.intellij.dsa.tree.trie

/**
 * Trie
 *
 * @author tech@intellij.io
 */
interface Trie {

  /**
   * Determines whether the trie is empty.
   */
  fun isEmpty(): Boolean = size() == 0

  /**
   * Retrieves the number of elements currently stored in the trie.
   */
  fun size(): Int

  /**
   * Adds the specified content to the trie.
   */
  fun addContent(content: String)

  /**
   * Determines whether the trie contains the specified content.
   */
  fun contains(content: String): Boolean

  /**
   * Checks whether the trie contains any content that partially matches the specified input.
   *
   * @param content the input content to check for partial matches in the trie
   * @return true if any portion of the specified content exists in the trie, false otherwise
   */
  fun containsPartial(content: String): Boolean

  /**
   * 获取片段分割的方法
   */
  fun getSegmentFunc(): (String) -> List<String>

  /**
   * cleared trie
   */
  fun clear()

}
