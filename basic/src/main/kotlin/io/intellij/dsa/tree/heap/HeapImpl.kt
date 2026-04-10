package io.intellij.dsa.tree.heap

import io.intellij.dsa.getLogger

/**
 * HeapImpl
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class HeapImpl<T> : Heap<T> {

  companion object {
    val log = getLogger(HeapImpl::class.java)
    const val DEFAULT_CAPACITY = 7
    val DEFAULT_HEAP_TYPE = HeapType.MIN_HEAP
  }

  private val type: HeapType
  private var comparator: Comparator<T>?

  private var count: Int = 0
  private var capacity: Int = DEFAULT_CAPACITY
  private var data: Array<Any?> = arrayOfNulls<Any?>(DEFAULT_CAPACITY)

  constructor() : this(DEFAULT_HEAP_TYPE, null)

  constructor(type: HeapType) : this(type, null)

  constructor(type: HeapType, comparator: Comparator<T>?) {
    this.type = type
    this.comparator = comparator
  }

  constructor(arr: Array<T>) : this(arr, DEFAULT_HEAP_TYPE, null)

  constructor(arr: Array<T>, type: HeapType) : this(arr, type, null)

  constructor(arr: Array<T>, type: HeapType, comparator: Comparator<T>? = null) {
    this.type = type
    this.comparator = comparator
    initArray(arr)
  }

  override fun size(): Int = this.count

  override fun add(value: T) {
    expand(count + 1)
    this.data[count++] = value
    siftUp(count - 1)
  }

  private fun expand(size: Int) {
    if (size <= capacity) {
      return
    }
    // 7 -> 15 -> 31
    val newCap = capacity * 2 + 1

    val newData = arrayOfNulls<Any?>(newCap)
    System.arraycopy(data, 0, newData, 0, capacity)
    log.debug("heap expand : $capacity -> $newCap")
    this.capacity = newCap
    this.data = newData
  }

  override fun peek(): T? {
    return if (isEmpty()) {
      null
    } else {
      @Suppress("UNCHECKED_CAST")
      data[0] as T
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun extract(): T? {
    return if (isEmpty()) {
      null
    } else {
      val extract = data[0] as T
      // 将最后一个元素放到根节点
      data[0] = data[count - 1]
      data[count - 1] = null
      count--
      siftDown(0)
      reduce(count)
      extract
    }
  }

  private fun reduce(size: Int) {
    if (size < DEFAULT_CAPACITY) {
      return
    }
    // 31 -> 15 -> 7
    val newCap = (capacity - 1) / 2
    if (size == newCap) {
      val newData = arrayOfNulls<Any?>(newCap)
      System.arraycopy(data, 0, newData, 0, newCap)
      log.debug("heap reduce : $capacity -> $newCap")
      this.capacity = newCap
      this.data = newData
    }
  }

  private fun compare(a: T, b: T): Boolean {
    return if (comparator != null) {
      if (type == HeapType.MIN_HEAP)
        comparator!!.compare(a, b) < 0
      else {
        comparator!!.compare(a, b) > 0
      }
    } else {
      // 检查 a 和 b 是否都实现了 Comparable 接口
      if (a is Comparable<*> && b is Comparable<*>) {
        @Suppress("UNCHECKED_CAST")
        val comparison = (a as Comparable<T>).compareTo(b)
        if (type == HeapType.MIN_HEAP)
          comparison < 0
        else
          comparison > 0
      } else {
        throw IllegalArgumentException("元素必须实现 Comparable 接口或提供 Comparator")
      }
    }
  }

  private fun siftUp(index: Int) {
    var cur = index
    var parent = (index - 1) / 2

    @Suppress("UNCHECKED_CAST")
    while (cur > 0 && compare(data[cur] as T, data[parent] as T)) {
      // 交换 当前节点 和 父节点
      data.swap(cur, parent)
      cur = parent
      parent = (cur - 1) / 2
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun siftDown(index: Int) {
    var curIndex = index
    var left = 2 * index + 1
    var right = 2 * index + 2

    // 左节点必须存在
    while (left <= count - 1) {
      // 假设左子节点更小
      var targetIndex = left

      // 实际右子节点存在并更小
      if (right <= count - 1 && compare(data[right] as T, data[left] as T)) {
        targetIndex = right
      }

      // 比较 子节点 与 当前节点
      if (compare(data[targetIndex] as T, data[curIndex] as T)) {
        data.swap(targetIndex, curIndex)
        curIndex = targetIndex
        left = 2 * curIndex + 1
        right = 2 * curIndex + 2
      } else {
        break
      }
    }
  }

  private fun initArray(arr: Array<T>) {
    if (arr.isEmpty()) {
      return
    }
    this.count = arr.size

    var calculateCap = DEFAULT_CAPACITY
    while (calculateCap < count) {
      calculateCap = calculateCap * 2 + 1
    }
    this.capacity = calculateCap

    this.data = arrayOfNulls<Any?>(calculateCap)
    System.arraycopy(arr, 0, this.data, 0, count)

    this.heapify()
  }

  private fun heapify() {
    // 从最后一个非叶子节点开始向下调整
    for (i in (count / 2 - 1) downTo 0) {
      siftDown(i)
    }
  }

  override fun getType(): HeapType = this.type

  override fun clear() {
    this.data = arrayOfNulls<Any?>(DEFAULT_CAPACITY)
    this.count = 0
    this.capacity = DEFAULT_CAPACITY
  }

  private fun Array<Any?>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
  }

}