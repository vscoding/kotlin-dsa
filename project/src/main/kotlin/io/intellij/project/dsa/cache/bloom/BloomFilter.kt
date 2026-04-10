package io.intellij.project.dsa.cache.bloom

/**
 * BloomFilter 布隆过滤器。
 *
 * @author tech@intellij.io
 */
interface BloomFilter {

  /**
   * 新增值
   */
  fun add(value: String)

  /**
   * 是否包含
   */
  fun contains(value: String): Boolean

}