package io.intellij.project.dsa.cache

import io.intellij.project.dsa.buildBloomFilter
import io.intellij.project.dsa.cache.bloom.HashUtils
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

/**
 * BloomFilterTest
 *
 * @author tech@intellij.io
 */
class BloomFilterTest {

  @Test
  fun `test hash utils iterator`() {
    val hashUtils = HashUtils(1, 32)
    val it = hashUtils.indicesIterator("hello", k = 6)
    while (it.hasNext()) {
      val bit = it.next()
      // 按需使用 bit
      println("bit: $bit")
    }
  }

  @Test
  fun `test bloom filter`() {
    val cl = Thread.currentThread().contextClassLoader
    val input = cl.getResourceAsStream("domain_apple.txt") ?: error("resource not found")

    val bloomFilter = buildBloomFilter(10000)
    input.bufferedReader().forEachLine {
      bloomFilter.add(it)
    }

    Assertions.assertTrue(bloomFilter.contains("www.apple.com"))
    Assertions.assertFalse(bloomFilter.contains("www.google.com"))
  }
}
