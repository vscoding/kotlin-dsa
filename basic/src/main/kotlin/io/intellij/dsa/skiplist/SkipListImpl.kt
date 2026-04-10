package io.intellij.dsa.skiplist

import kotlin.random.Random

/**
 * SkipListImpl
 *
 * @author tech@intellij.io
 */
class SkipListImpl<K : Comparable<K>, V>(
  val maxLevel: Int = DEFAULT_MAX_LEVEL,
  val p: Double = DEFAULT_P,
) : SkipList<K, V> {

  init {
    require(maxLevel > 0) { "maxLevel must be greater than 0" }
    require(p > 0.0 && p <= 1.0) { "p must be in (0, 1]" }
  }

  companion object {
    private const val MIN_LEVEL = 1
  }

  /**
   * 随机
   */
  private val random = Random(System.currentTimeMillis())

  /**
   * 哨兵节点
   */
  private val head = SkipListNode<K, V>(null, null, this.maxLevel, this.p, true)

  /**
   * 跳表的层级，取决于跳表节点创建的随机出的层级
   */
  private var skipListLevel = MIN_LEVEL

  /**
   * 跳表的节点个数
   */
  private var nodeCount = 0

  /**
   * 跳表节点个数
   */
  override fun size(): Int = nodeCount

  /**
   * 当前跳表存储的层级
   */
  override fun level(): Int = this.skipListLevel

  override fun contains(key: K): Boolean {
    return get(key) != null
  }

  /**
   * 查找的逻辑 跳表查找的核心就是 从上到下、每层尽量向右，最后在底层确认
   *
   * 高层 → 定位范围 低层 → 精确查找（最终确认节点）
   *
   * 优先向右查找，如果不满足，再向下查找
   */
  override fun get(key: K): V? {
    if (isEmpty()) return null
    // 从哨兵节点 head 开始查找，所有的值都在 node.forward[level] 里面
    // 两个维度，一个处理同行（向右），一个处理同列（向下）
    var node = head
    var levelIdx = level() - 1

    while (levelIdx >= 0) {
      val storageNode = node.getRight(levelIdx)
      if (storageNode == null) {
        // 存储值的节点不存在，直接向下查找
        levelIdx--
      } else { // 存储值的节点存在，作比较
        val currKey = storageNode.key!!
        when {
          currKey == key -> return storageNode.value
          key > currKey -> {
            // 继续向右查找
            node = storageNode
          }

          else -> { // key < currKey 向下查找
            levelIdx--
          }
        }
      }
    }
    return null
  }

  override fun insert(key: K, value: V) {
    // 每一层 默认 head 为前驱节点
    // 前驱的核心是每一层最后一个小于key的，每一层本身就是顺序列表，所以不需要从Head再遍历
    val update = Array(maxLevel) { head }
    var currNode = head
    var levelIdx = level() - 1 // 从存储的最高的level开始，避免遍历无效的层数

    while (levelIdx >= 0) {
      val storageNode = currNode.getRight(levelIdx)
      if (storageNode == null) {
        update[levelIdx] = currNode
        levelIdx--
      } else {
        val storageKey = storageNode.key!!
        when {
          key == storageKey -> {
            // 更新的时候发现匹配到了值,直接更新，并退出
            storageNode.value = value
            return
          }

          key > storageKey -> {
            // 继续向右查找,刷新当前的node
            currNode = storageNode
          }

          else -> { // key < storageKey
            update[levelIdx] = currNode  // 这一层找到了
            levelIdx-- // 继续向下
          }
        }
      }
    }

    // 真实的插入操作
    // 1. update中需要指向新的节点
    // 2. 插入的节点需要更新索引
    // 3. 那么从思路上来说，需要从下往上更新
    val newNode = SkipListNode(key, value, this.maxLevel, this.p)

    val newNodeLevel = newNode.getNodeLevel().apply {
      if (this > skipListLevel) { // 更新levelCount
        skipListLevel = this
      }
    }

    // kotlin 1.9 新语法 ..< 左闭右开
    // for (levelIndex in 0..<newNodeLevel) {
    for (levelIndex in 0 until newNodeLevel) {
      val updateNode = update[levelIndex] // 由于哨兵节点的设计，前驱节点一定存在
      val updateRight = updateNode.getRight(levelIndex)
      newNode.setRight(levelIndex, updateRight)
      updateNode.setRight(levelIndex, newNode)
    }

    nodeCount++
  }

  override fun remove(key: K): V? {
    // 删除节点的前驱节点
    val update = arrayOfNulls<SkipListNode<K, V>>(level())
    var node = head
    var levelIdx = level() - 1 // 删除的时候对于节点的更新不需要从最高层

    var target: SkipListNode<K, V>? = null

    while (levelIdx >= 0) {
      val storage = node.getRight(levelIdx)
      if (storage == null) {
        levelIdx--
      } else {
        val storageKey = storage.key!!
        when {
          storageKey == key -> { // 找到需要删除的节点
            target = storage
            update[levelIdx] = node
            levelIdx-- // 继续寻找下一层
          }

          storageKey < key -> { // 目前的值小于需要删除的值
            node = storage // 继续向右查找
          }

          else -> { // storageKey > key
            levelIdx-- // 继续向下查找
          }
        }
      }
    }

    if (target == null) {
      println("not find target")
      return null
    }

    // 更新索引
    // 1. 对于被删除的节点,更新前驱节点的指向
    // 2. 如果前驱节点没有指向过当前节点，则不管
    // 3. 如果前驱节点指向了当前节点，更新到指向的节点的新节点
    val targetLevel = target.getNodeLevel()

    // 从最底层开始更新
    for (levelIndex in 0 until targetLevel) {
      // 如果存在前驱节点则更新
      update[levelIndex]?.apply {
        val left = this
        left.setRight(levelIndex, target.getRight(levelIndex))
      }
    }

    // 节点被删除了，重新更新跳表本身的层高，不能确定哪个是MAX，从上到下遍历一遍
    // 4) 收缩最高层级：当顶层为空时向下收缩
    while (skipListLevel > MIN_LEVEL && head.getRight(skipListLevel - 1) == null) {
      skipListLevel--
    }


    nodeCount--

    return target.value
  }

  override fun clear() {
    this.skipListLevel = MIN_LEVEL
    this.nodeCount = 0

    for (i in 0 until head.getNodeLevel()) {
      head.setRight(i, null)
    }
  }

  private inner class SkipListNode<K : Comparable<K>, V>(
    val key: K?,   // 不可变，唯一标识
    var value: V?, // 可变，更新值
    val maxLevel: Int,
    val p: Double,
    head: Boolean = false,
  ) {

    // 节点的层数： 跳表的核心 概率化
    private val nodeLevel: Int = if (head) maxLevel else randomLevel()

    // 本质上由 forward[0] 构成了链表，其他层的都是索引
    private val forward: Array<SkipListNode<K, V>?> = arrayOfNulls(maxLevel)

    /**
     * 获取指向的值
     */
    fun getRight(level: Int): SkipListNode<K, V>? = forward[level]


    /**
     * 设置指向的值
     */
    fun setRight(level: Int, node: SkipListNode<K, V>?) {
      forward[level] = node
    }

    /**
     * 获取节点的随机层数
     */
    fun getNodeLevel(): Int = this.nodeLevel

    /**
     * 随机生成一个层数 [1, MAX_LEVEL],最终体现在创建的数组的大小上
     */
    private fun randomLevel(): Int {
      var level = 1
      while (random.nextDouble() < p && level < this.maxLevel) {
        level++
      }
      return level
    }
  }

  override fun print() {
    if (isEmpty()) {
      println("SkipList(empty)")
      return
    }

    // 底层（level 0）所有节点，作为列对齐基准；首列放入 HEAD
    val columns = mutableListOf<SkipListNode<K, V>>()
    columns.add(head)
    var n = head.getRight(0)
    while (n != null) {
      columns.add(n)
      n = n.getRight(0)
    }

    // 计算对齐宽度（考虑 HEAD 与所有 key 的宽度）
    val maxKeyLen = columns.drop(1).maxOfOrNull { it.key.toString().length } ?: 0
    val cellWidth = maxOf("(HEAD)".length, maxKeyLen) + 2
    val arrowToken = "-> "
    val arrowSpace = " ".repeat(arrowToken.length)

    // 自顶向下逐层打印
    for (lvl in level() - 1 downTo 0) {
      // 1) 该层的 incoming 目标集合：凡是在该层能被某个前驱指到的节点，都算 incoming
      val incoming = HashSet<SkipListNode<K, V>>()
      var from = head.getRight(lvl)
      while (from != null) {
        incoming.add(from)           // from 在该层有前驱（head 或上一节点）
        from = from.getRight(lvl)
      }

      val sb = StringBuilder().apply { append("Level ").append(lvl).append(": ") }

      // 2) 逐列打印：该列存在则打印 key；在“列间空隙”处，如果右侧列是 incoming，则画箭头
      for (i in columns.indices) {
        val node = columns[i]
        val label = if (node === head) "(HEAD)"
        else if (lvl < node.getNodeLevel()) node.key.toString()
        else ""
        sb.append(label.padEnd(cellWidth, ' '))

        if (i < columns.lastIndex) {
          val rightNode = columns[i + 1]
          val drawArrow = incoming.contains(rightNode) // 右侧列若在本层为 incoming，则在这里画箭头
          sb.append(if (drawArrow) arrowToken else arrowSpace)
        }
      }
      println(sb.toString().trimEnd())
    }
  }

}
