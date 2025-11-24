package io.intellij.project.dsa.cache.bloom

import kotlin.math.max

/**
 * HashUtils
 *
 * 提供多种字符串哈希函数，并统一映射为 [0, size) 的索引。
 * 适用于 Bloom Filter 等需要多哈希函数的场景。
 *
 * @param seed 哈希扰动种子（非严格要求 >0，但建议使用正数）
 * @param size 映射空间大小（通常为 BitSet 的位数），必须 > 0
 */
class HashUtils(private val seed: Int, private val size: Int) {

    init {
        require(size > 0) { "size must be > 0" }
    }

    /**
     * 返回一组稳定的索引（长度为 k），每个索引都在 [0, size)。
     * 当需要 k 个哈希函数时，可基于双重散列思想由两个/多个基础哈希组合得到。
     */
    fun indices(value: String, k: Int = 4): IntArray {
        require(k > 0) { "k must be > 0" }
        // 基础哈希（保持稳定且成本较低）
        val h1 = mix1(value)
        val h2 = mix2(value)
        val h3 = mix3(value)
        val h4 = mix4(value)

        // 使用组合方式生成第 i 个索引，避免只依赖单一 hash
        // idx(i) = floorMod(h1 + i*h2 + i*i*h3 + seedXor, size)
        val result = IntArray(k)
        val seedXor = seed * 0x9E3779B9.toInt() // 与黄金比例常数相关的扰动
        for (i in 0 until k) {
            val ii = i + 1
            val combined = h1 + ii * h2 + ii * ii * h3 + (h4 xor seedXor)
            result[i] = indexFrom(combined)
        }
        return result
    }

    /**
     * 惰性计算版本：返回一个迭代器，每次 next() 只计算一个索引。
     * - 仅在创建时计算 h1~h4，避免重复工作
     * - 可用 hasNext() 控制计算次数
     */
    fun indicesIterator(value: String, k: Int = 4): IndexIterator {
        require(k > 0) { "k must be > 0" }
        val h1 = mix1(value)
        val h2 = mix2(value)
        val h3 = mix3(value)
        val h4 = mix4(value)
        return IndexIterator(h1, h2, h3, h4, k)
    }

    /**
     * 内部惰性生成器：每次 next() 计算一个索引
     */
    inner class IndexIterator(
        private val h1: Int,
        private val h2: Int,
        private val h3: Int,
        private val h4: Int,
        private val k: Int
    ) {
        private var i: Int = 0
        private val seedXor: Int = seed * 0x9E3779B9.toInt()

        fun hasNext(): Boolean = i < k

        fun next(): Int {
            if (!hasNext()) throw NoSuchElementException("No more indices")
            val ii = i + 1
            val combined = h1 + ii * h2 + ii * ii * h3 + (h4 xor seedXor)
            i++
            return indexFrom(combined)
        }
    }

    fun hashG1(value: String): Int {
        var hash = 0
        for (c in value) {
            hash += (hash shl 5) + c.code // hash * 33 + c
            hash = toNonNegative(hash)
        }
        return indexFrom(hash)
    }

    fun hashG2(value: String): Int {
        var hash = 7397
        for (c in value) {
            hash += (hash shl 5) + c.code // hash * 33 + c
        }
        // 保持正数并映射到 [0, size)
        return indexFrom(hash)
    }

    fun hashG3(value: String): Int {
        var hash = 0
        for (c in value) {
            hash += (hash shl 5) + c.code // hash * 33 + c
            hash += c.code
        }
        return indexFrom(hash)
    }

    fun hashG4(value: String): Int {
        // 近似 Java 的 (h ^ (h >>> 16)) 的扰动
        var h = value.hashCode()
        h = h xor (h ushr 16)
        // 再与 seed 及 size 相关的位运算混合
        val mixed = (seed * max(1, size - 1)) and h
        return indexFrom(mixed)
    }

    private fun mix1(value: String): Int {
        var hash = 5381 // 常见起始值
        for (c in value) {
            hash += (hash shl 5) + c.code // djb2 变体
        }
        return toNonNegative(hash)
    }

    private fun mix2(value: String): Int {
        var hash = 0
        for (c in value) {
            hash = hash * 31 + c.code // 类似 String.hashCode 的线性滚动
        }
        // 引入 seed 扰动
        return toNonNegative(hash xor (seed * 0x85EBCA6B.toInt()))
    }

    private fun mix3(value: String): Int {
        var hash = 216613626 // FNV-like 起始值（缩短版）
        for (c in value) {
            hash = (hash xor c.code) * 16777619
        }
        return toNonNegative(hash)
    }

    private fun mix4(value: String): Int {
        // 使用 JDK 的 hashCode 并做一次轻量搅拌
        var h = value.hashCode()
        h = h xor (h ushr 16)
        h *= 0x7feb352d
        h = h xor (h ushr 15)
        h *= 0x846ca68b.toInt()
        h = h xor (h ushr 16)
        return toNonNegative(h)
    }

    private fun indexFrom(hash: Int): Int = floorMod(hash, size)

    private fun toNonNegative(x: Int): Int = x and Int.MAX_VALUE

    private fun floorMod(x: Int, m: Int): Int {
        // 与 Java Math.floorMod 行为一致
        var r = x % m
        if (r < 0) r += m
        return r
    }

}