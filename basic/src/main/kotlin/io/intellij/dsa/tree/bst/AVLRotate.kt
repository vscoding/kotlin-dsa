package io.intellij.dsa.tree.bst

import io.intellij.dsa.getLogger

val log = getLogger(AVLTree::class.java)

internal enum class Action {
    ADD, REMOVE
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.getBalanceFactor(): Int =
    getNodeHeight(this.getLeft()) - getNodeHeight(this.getRight())

internal fun Int.valueIn(min: Int, max: Int): Boolean {
    require(max >= min) {
        "max should be greater than or equal to min"
    }
    return this in min..max
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.rebalance(action: Action): BSTNode<K, V> {
    val balanceFactor = this.getBalanceFactor()
    if (balanceFactor.valueIn(-1, 1)) {
        return this
    }

    log.debug("balanceFactor: {}, action: {}, node: {}", balanceFactor, action, this)
    return when (action) {
        Action.ADD -> {
            // Rebalances left‑heavy node after insertion via rotations
            when {
                (balanceFactor == 2 && this.getLeft()!!.getBalanceFactor() == 1) -> this.ll()
                (balanceFactor == 2 && this.getLeft()!!.getBalanceFactor() == -1) -> this.lr()
                (balanceFactor == -2 && this.getRight()!!.getBalanceFactor() == -1) -> this.rr()
                (balanceFactor == -2 && this.getRight()!!.getBalanceFactor() == 1) -> this.rl()
                else -> throw IllegalStateException("balanceFactor: $balanceFactor, action: $action, node: $this")
            }
        }

        Action.REMOVE -> {
            // Rebalances right‑heavy node after removal via rotations
            when {
                (balanceFactor == 2 && this.getLeft()!!.getBalanceFactor() >= 0) -> this.ll()
                (balanceFactor == 2 && this.getLeft()!!.getBalanceFactor() < 0) -> this.lr()
                (balanceFactor == -2 && this.getRight()!!.getBalanceFactor() <= 0) -> this.rr()
                (balanceFactor == -2 && this.getRight()!!.getBalanceFactor() > 0) -> this.rl()
                else -> throw IllegalStateException("balanceFactor: $balanceFactor, action: $action, node: $this")
            }
        }
    }

}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.ll(): BSTNode<K, V> {
    return this.rotateNodeAndLeft()
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.rr(): BSTNode<K, V> {
    return this.rotateNodeAndRight()
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.lr(): BSTNode<K, V> {
    /*
        x
       /
       y
        \
        z
     */
    return this.setLeft(
        this.getLeft()?.rotateNodeAndRight()
    ).updateHeight().rotateNodeAndLeft()
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.rl(): BSTNode<K, V> {
    /*
       x
        \
         y
        /
      z
    */

    return this.setRight(
        this.getRight()?.rotateNodeAndLeft()
    ).updateHeight().rotateNodeAndRight()
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.rotateNodeAndRight(): BSTNode<K, V> {
    /*
        n                 x
       / \               / \
      ?   x            n   b?
         / \          / \
        a?  b?       ?  a?
     */
    val x = this.getRight() ?: return this
    // 先更新下层节点的高度
    this.setRight(x.getLeft()).updateHeight()
    // 再更新新节点的高度
    return x.setLeft(this).updateHeight()
}

internal fun <K : Comparable<K>, V> BSTNode<K, V>.rotateNodeAndLeft(): BSTNode<K, V> {
    /*
            n              x
           / \             / \
          x   ?          a?  n
         / \                / \
        a?  b?             b?  ?
     */
    val x = this.getLeft() ?: return this
    this.setLeft(x.getRight()).updateHeight()
    return x.setRight(this).updateHeight()
}
