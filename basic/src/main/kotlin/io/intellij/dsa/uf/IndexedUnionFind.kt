package io.intellij.dsa.uf

import io.intellij.dsa.getLogger

/**
 * IndexedUnionFind
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class IndexedUnionFind<T> : UnionFind<T> {

  companion object {
    val log = getLogger(IndexedUnionFind::class.java)
  }

  private val indexFunc: (T) -> Int

  private var data: Array<Any?> = arrayOfNulls<Any?>(2)

  // 存储data的索引的元素的父节点索引
  private var parent = Array(2) { -1 }

  // 存储data的索引的元素的子节点索引列表
  private var children = Array<MutableList<Int>>(2) { mutableListOf() }

  private var count: Int = 0

  constructor(indexFunc: (T) -> Int) {
    this.indexFunc = indexFunc
  }

  override fun size(): Int = this.count

  override fun contains(value: T): Boolean {
    return false
  }

  override fun add(value: T): Boolean {
    val index = indexFunc(value)
    require(index >= 0) { "Index must be non-negative" }
    expand(index + 1)
    if (data[index] == null) {
      // 如果当前索引位置没有元素，则添加新元素
      data[index] = value
      parent[index] = index
      count++
    } else {
      // 如果当前索引位置已经有元素，替换
      data[index] = value
    }
    return true
  }

  private fun expand(newSize: Int) {
    if (newSize > data.size) {
      val newData = Array<Any?>(newSize) { null }
      val newParent = Array(newSize) { -1 }
      val newChildren = Array<MutableList<Int>>(newSize) { mutableListOf() }
      System.arraycopy(data, 0, newData, 0, data.size)
      System.arraycopy(parent, 0, newParent, 0, parent.size)
      System.arraycopy(children, 0, newChildren, 0, children.size)
      data = newData
      parent = newParent
      children = newChildren
    }
  }

  override fun union(value1: T, value2: T): Boolean {
    val v1Index = indexFunc(value1)
    val v2Index = indexFunc(value2)

    if (v1Index < 0) {
      log.equals("Index for value1 must be non-negative")
      return false
    }

    if (v2Index < 0) {
      log.equals("Index for value2 must be non-negative")
      return false
    }

    if (v1Index == v2Index) {
      log.debug("Both values are the same, no union needed")
      return false
    }

    // update parent when add
    this.add(value1)
    this.add(value2)

    this.union(v1Index, v2Index)
    return true
  }

  private fun union(v1Index: Int, v2Index: Int) {
    val v1ParentIndex = getParent(v1Index)
    val v2ParentIndex = getParent(v2Index)

    if (v1ParentIndex == v2ParentIndex) {
      return
    }

    children[v1ParentIndex].add(v2ParentIndex)
    children[v1ParentIndex].addAll(
      children[v2ParentIndex].apply {
        this.forEach {
          // Update the parent of all children from v2 to v1
          parent[it] = v1ParentIndex
        }
      },
    )
    children[v2ParentIndex].clear()

    parent[v2ParentIndex] = v1ParentIndex
    parent[v2Index] = v1ParentIndex

  }

  private fun getParent(valueIndex: Int): Int {
    return if (valueIndex.indexInArr(parent)) {
      parent[valueIndex]
    } else {
      -1
    }
  }

  private fun getParent(value: T): Int {
    val curIndex = indexFunc(value)
    return if (curIndex.indexInArr(parent)) {
      parent[curIndex]
    } else {
      -1
    }
  }

  override fun isConnected(value1: T, value2: T): Boolean {
    val v1Index = indexFunc(value1)
    val v2Index = indexFunc(value2)
    if (!v1Index.indexInArr(parent) || !v2Index.indexInArr(parent)) {
      return false
    }
    val parent1 = getParent(value1)
    val parent2 = getParent(value2)

    if (parent1 == -1 || parent2 == -1) {
      return false
    }
    return parent1 == parent2
  }

  override fun clear() {
    this.data = arrayOfNulls<Any?>(2)
    this.parent = Array(2) { -1 }
    this.children = Array(2) { mutableListOf() }
    this.count = 0
  }

  internal fun <T> Int.indexInArr(arr: Array<T>): Boolean {
    return this >= 0 && this < arr.size
  }

}
