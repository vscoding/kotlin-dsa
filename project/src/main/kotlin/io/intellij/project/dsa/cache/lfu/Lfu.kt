package io.intellij.project.dsa.cache.lfu

/**
 * LFU（Least Frequently Used，最不经常使用）是一种缓存淘汰策略：当缓存满了，需要删除一个键值时，优先淘汰“访问次数最少”的那个键
 *
 * - 每个键有一个“访问频次”计数，get/put 命中都会让频次 +1
 * - 淘汰时选择全局频次最低的键；若有多个频次相同，一般再用“最近最少使用”（LRU）作为并列打破原则。
 *
 * @author dev@intellij.io
 */
interface Lfu<K, V> {

}
