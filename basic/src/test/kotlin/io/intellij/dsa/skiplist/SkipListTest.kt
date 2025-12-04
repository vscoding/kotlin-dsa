package io.intellij.dsa.skiplist

import io.intellij.dsa.TreeBuilder
import org.junit.jupiter.api.Test

/**
 * SkipListTest
 *
 * @author tech@intellij.io
 */
class SkipListTest {

    @Test
    fun `test skip list`() {
        val skipList = TreeBuilder.buildSkipList<Int, String>()
        val intArr = Array(15) { it + 1 }
        val list = intArr.toMutableList()
        list.shuffle() // 打乱
        for (i in intArr.indices) {
            skipList.insert(list[i], "value-${list[i]}")
        }
        println("skipList size = ${skipList.size()}")
        skipList.print()
    }


    @Test
    fun `test skip list get`() {
        val skipList = TreeBuilder.buildSkipList<Int, String>()

        val size = 100000

        val intArr = Array(size) { it + 1 }
        val list = intArr.toMutableList()
        list.shuffle() // 打乱
        for (i in intArr.indices) {
            skipList.insert(list[i], "value-${list[i]}")
        }

        val getKey = size / 2
        val get = skipList.get(getKey)

        println("get key = $getKey, value = $get")
    }

    @Test
    fun `test skip list delete`() {
        val skipList = TreeBuilder.buildSkipList<Int, String>()

        val size = 100000

        val intArr = Array(size) { it + 1 }
        val list = intArr.toMutableList()
        list.shuffle() // 打乱
        for (i in intArr.indices) {
            skipList.insert(list[i], "value-${list[i]}")
        }

        for (i in 1..size) {
            skipList.remove(i)
        }
        println("skipList size = ${skipList.size()}")

    }


}
