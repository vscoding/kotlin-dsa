package io.intellij.dsa.skiplist

import io.intellij.dsa.tree.TreeBuilder
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * SkipListTest
 *
 * @author dev@intellij.io
 */
class SkipListTest {

  @Test
  fun `skip list stores shuffled entries and prints its levels`() {
    val skipList = TreeBuilder.buildSkipList<Int, String>()
    val intArr = Array(15) { it + 1 }
    val list = intArr.toMutableList()
    list.shuffle(Random(0)) // 固定乱序，便于复现
    for (i in intArr.indices) {
      skipList.insert(list[i], "value-${list[i]}")
    }

    println("skipList size = ${skipList.size()}")
    skipList.print()

    assertEquals(intArr.size, skipList.size())
    assertTrue(intArr.all(skipList::contains))
  }

  @Test
  fun `skip list returns the value associated with a key`() {
    val skipList = TreeBuilder.buildSkipList<Int, String>()
    val size = 128
    val intArr = Array(size) { it + 1 }
    val list = intArr.toMutableList()
    list.shuffle(Random(0))
    for (i in intArr.indices) {
      skipList.insert(list[i], "value-${list[i]}")
    }

    val getKey = size / 2
    val get = skipList.get(getKey)

    println("get key = $getKey, value = $get")

    assertEquals("value-$getKey", get)
    assertNull(skipList.get(size + 1))
  }

  @Test
  fun `skip list removes every stored entry`() {
    val skipList = TreeBuilder.buildSkipList<Int, String>()
    val size = 64
    val intArr = Array(size) { it + 1 }
    val list = intArr.toMutableList()
    list.shuffle(Random(0))
    for (i in intArr.indices) {
      skipList.insert(list[i], "value-${list[i]}")
    }

    for (i in 1..size) {
      assertEquals("value-$i", skipList.remove(i))
      assertFalse(skipList.contains(i))
    }
    println("skipList size = ${skipList.size()}")

    assertTrue(skipList.isEmpty())
    assertNull(skipList.remove(size + 1))
  }

}
