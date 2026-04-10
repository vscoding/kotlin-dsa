package io.intellij.dsa.tree.bst

const val DEFAULT_HEIGHT = 1

internal fun <K : Comparable<K>, V> isBST(node: BSTNode<K, V>?): Boolean {
  return isBSTHelper(node, null, null)
}

private fun <K : Comparable<K>, V> isBSTHelper(node: BSTNode<K, V>?, minVal: K?, maxVal: K?): Boolean {
  if (node == null) return true
  val key = node.getKey()

  // 检查当前节点是否违反边界条件
  if ((minVal != null && key <= minVal) || (maxVal != null && key >= maxVal)) {
    return false
  }

  // 递归检查左右子树，更新边界
  return isBSTHelper(node.getLeft(), minVal, key) &&
      isBSTHelper(node.getRight(), key, maxVal)
}

internal fun <K : Comparable<K>, V> getNode(node: BSTNode<K, V>?, k: K): BSTNode<K, V>? {
  return if (node == null) {
    null
  } else {
    when {
      k < node.getKey() -> getNode(node.getLeft(), k)
      k > node.getKey() -> getNode(node.getRight(), k)
      else -> node // Found the node
    }
  }
}

internal fun <K : Comparable<K>, V> getMin(node: BSTNode<K, V>?): BSTNode<K, V>? {
  return node?.getLeft()?.let { getMin(it) } ?: node
}

internal fun <K : Comparable<K>, V> getMax(node: BSTNode<K, V>?): BSTNode<K, V>? {
  return node?.getLeft()?.let { getMax(it) } ?: node
}

internal fun <K : Comparable<K>, V> printBST(root: BSTNode<K, V>?) {
  if (root == null) {
    println("空树")
    return
  }

  val height = root.getHeight()
  val rows = height * 2
  val cols = (1 shl (height + 1)) - 1  // 2^(height+1) - 1

  val matrix = Array(rows) { Array(cols) { " " } }

  // 填充矩阵
  fillMatrixSimplified(matrix, root, 0, 0, cols - 1)

  // 打印矩阵，跳过全空行
  matrix.forEach { row ->
    if (row.any { it != " " }) {
      row.forEach { print(it) }
      println()
    }
  }
}

private fun <K : Comparable<K>, V> fillMatrixSimplified(
  matrix: Array<Array<String>>,
  node: BSTNode<K, V>?,
  row: Int,
  left: Int,
  right: Int,
) {
  if (node == null || row >= matrix.size) {
    return
  }

  val mid = (left + right) / 2

  // 放置当前节点
  if (mid in matrix[0].indices) {
    matrix[row][mid] = node.getKey().toString()
  }

  // 间隔行用于放置连接线
  val nextRow = row + 2

  // 处理左子树
  node.getLeft()?.let { leftChild ->
    // 计算左子节点的位置
    val leftChildMid = (left + mid - 1) / 2

    // 绘制简化连接线 (只在中间位置放一个/)
    if (row + 1 < matrix.size) {
      val connectorPos = (mid + leftChildMid) / 2
      if (connectorPos in matrix[0].indices) {
        matrix[row + 1][connectorPos] = "/"
      }
    }

    // 递归填充左子树
    fillMatrixSimplified(matrix, leftChild, nextRow, left, mid - 1)
  }

  // 处理右子树
  node.getRight()?.let { rightChild ->
    // 计算右子节点的位置
    val rightChildMid = (mid + 1 + right) / 2

    // 绘制简化连接线 (只在中间位置放一个\)
    if (row + 1 < matrix.size) {
      val connectorPos = (mid + rightChildMid) / 2
      if (connectorPos in matrix[0].indices) {
        matrix[row + 1][connectorPos] = "\\"
      }
    }

    // 递归填充右子树
    fillMatrixSimplified(matrix, rightChild, nextRow, mid + 1, right)
  }

}