package io.intellij.project.dsa.cache.bloom

import java.util.BitSet

/**
 * BloomFilterImpl
 *
 * @author tech@intellij.io
 */
class BloomFilterImpl(bitCount: Int) : BloomFilter {
  private val hashUtils = HashUtils(1, bitCount)
  private val bitSet: BitSet = BitSet(bitCount)

  override fun add(value: String) {
    hashUtils.indices(value).forEach {
      bitSet.set(it)
    }
  }

  /**
   * 是否包含，通过多次hash计算出的值
   */
  override fun contains(value: String): Boolean {
    val iterator = hashUtils.indicesIterator(value)
    while (iterator.hasNext()) {
      iterator.next().let {
        if (!bitSet.get(it)) {
          return false
        }
      }
    }
    return true
  }

}
