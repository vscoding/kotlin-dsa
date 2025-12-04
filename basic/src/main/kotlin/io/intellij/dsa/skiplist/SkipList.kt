package io.intellij.dsa.skiplist

import io.intellij.dsa.DataStructurePrintable
import io.intellij.dsa.KVOperator

/**
 * SkipList 跳表是对有序链表的改进
 *
 * @author tech@intellij.io
 */
interface SkipList<K : Comparable<K>, V> : KVOperator<K, V>, DataStructurePrintable {

    /**
     * 跳表的层数
     */
    fun level(): Int

}