package io.intellij.project.dsa.cache.lru

import io.intellij.dsa.DataStructurePrintable
import io.intellij.dsa.KVOperator

/**
 * LRU（Least Recently Used，最近最少使用）是一种常见的缓存淘汰策略：当缓存容量满时，优先淘汰“最近最久没有被访问”的数据项。
 *
 * - 核心思想：越“久未使用”的数据越不重要，先被淘汰
 * - 命中时机：每次 get 或 put 更新已有键，都将该键标记为“最近使用”
 * - 淘汰规则：容量满时移除最久未被访问的那一项。
 *
 * @author tech@intellij.io
 */
interface Lru<K, V> : KVOperator<K, V>, DataStructurePrintable {

}
