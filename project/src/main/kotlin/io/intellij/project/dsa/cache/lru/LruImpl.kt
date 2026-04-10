package io.intellij.project.dsa.cache.lru

/**
 * LruImpl LRU的实现主要是基于 HashMap + 双向链表
 *
 * @author tech@intellij.io
 */
class LruImpl<K, V>(
  val maxSize: Int = 100,
) : Lru<K, V> {

  private val map = mutableMapOf<K, LruNode>()
  private val head = LruNode(null, null)
  private val tail = LruNode(null, null)
  private var size = 0

  // 初始化双向链表的哨兵节点配置
  init {
    head.next = tail
    tail.prev = head
  }

  override fun size() = this.size

  override fun contains(key: K): Boolean {
    return map.containsKey(key)
  }

  override fun get(key: K): V? {
    return if (!contains(key)) {
      null
    } else {
      map[key]!!.let {
        moveToFirst(it.takeOut())
        it.value!!
      }
    }
  }

  /**
   * 将查询到的节点更新到双向链表的最前面
   */
  private fun moveToFirst(node: LruNode) {
    // 只有一个节点，或者当前节点的前一个节点已经哨兵head
    if (this.maxSize == 1 || node.prev == head) {
      return
    }
    // node节点放到第一位
    val first = head.next!!
    node.next = first
    node.prev = head
    first.prev = node
    head.next = node
  }

  override fun insert(key: K, value: V) {
    val tryGet = get(key)
    // 缓存存在
    if (tryGet != null) {
      map[key]!!.value = value
      return
    }

    // 缓存已满
    if (this.size >= this.maxSize) {
      removeLast()
      moveToFirst(LruNode(key, value))
      return
    }

    // 缓存未满
    map[key] = LruNode(key, value)
    this.size++
    moveToFirst(map[key]!!)
  }

  /**
   * 删除链表最后的节点
   */
  private fun removeLast() {
    if (this.size <= 1) {
      return
    }
    val last = tail.prev!!
    last.takeOut()
  }

  /**
   * 删除链表中某个节点
   */
  override fun remove(key: K): V? {
    return if (!contains(key)) {
      null
    } else {
      this.size--
      map.remove(key)!!.let {
        val rtValue = it.value
        it.takeOut()
        rtValue
      }
    }
  }

  /**
   * 清空链表
   */
  override fun clear() {
    this.size = 0
    this.map.clear()
    this.head.next = tail
    this.tail.prev = head
  }

  /**
   * 打印链表
   */
  override fun print() {
    val sb = StringBuilder()
    sb.append("[HEAD] ")
    var node = head.next!!
    while (node !== tail) {
      // sb.append("${node.key}:${node.value}")
      sb.append("${node.key}")
      if (node.next !== tail) sb.append(" <-> ")
      node = node.next!!
    }
    sb.append(" [TAIL]")
    println(sb.toString())
  }

  /**
   * Lru双向链表的节点
   */
  private inner class LruNode(val key: K?, var value: V?) {
    var prev: LruNode? = null
    var next: LruNode? = null
  }

  /**
   * 拿出节点
   */
  private fun LruNode.takeOut(): LruNode {
    this.prev!!.next = this.next
    this.next!!.prev = this.prev
    this.next = null
    this.prev = null
    return this
  }

}
