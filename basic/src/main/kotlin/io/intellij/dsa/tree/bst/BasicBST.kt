package io.intellij.dsa.tree.bst

/**
 * BasicBST
 *
 * @author tech@intellij.io
 * @since 2025-05-30
 */
class BasicBST<K : Comparable<K>, V> : BST<K, V> {
    private var root: BSTNode<K, V>? = null
    private var count = 0

    override fun size(): Int = this.count

    override fun getRoot(): BSTNode<K, V>? = this.root

    /**
     * Adds a key-value pair to the binary search tree (BST). If the key already exists,
     * updates the value associated with the key.
     *
     * @param key the key to insert or update in the BST
     * @param value the value to associate with the specified key
     */
    override fun insert(key: K, value: V) {
        root = add(root, key, value)
    }

    private fun add(node: BSTNode<K, V>?, k: K, v: V): BSTNode<K, V> {
        if (node == null) {
            count++
            return BasicBSTNode(k, v)
        }

        when {
            k < node.getKey() -> node.setLeft(add(node.getLeft(), k, v))

            k > node.getKey() -> node.setRight(add(node.getRight(), k, v))

            else -> {
                node.setValue(v)
            }
        }
        return node.updateHeight()
    }

    override fun remove(key: K): V? {
        return getNode(key)?.let { node ->
            val rtV = node.getValue()
            root = doRemove(root, key)
            rtV
        }
    }

    /**
     * Removes a node with the specified key from the binary search tree (BST).
     * If the key exists, the corresponding node is removed, and the tree structure
     * is adjusted to maintain the BST properties.
     *
     * @param node the current node being evaluated during the recursive traversal
     * @param k the key of the node to be removed
     * @return the updated subtree after removing the specified node
     */
    private fun doRemove(node: BSTNode<K, V>?, k: K): BSTNode<K, V>? {
        if (node == null) {
            return null
        }
        when {
            k < node.getKey() -> {
                node.setLeft(doRemove(node.getLeft(), k))
            }

            k > node.getKey() -> {
                node.setRight(doRemove(node.getRight(), k))
            }

            else -> {
                // Node to be removed found
                if (node.getLeft() == null && node.getRight() == null) {
                    // 左右子节点都为空
                    this.count--
                    return null
                }
                // 剩余三种情况  左不空+右不空 左不空+右空  左空+右不空
                node.getLeft()?.let { leftChild ->
                    // 左不空+右不空 左不空+右空
                    getMax(leftChild)!!.let { leftMax ->
                        node.setKey(leftMax.getKey())
                            .setValue(leftMax.getValue())
                            .setLeft(doRemove(leftChild, leftMax.getKey()))
                    }
                } ?: run {
                    // 左空+右不空：选择右子树最小值作为替换节点
                    getMin(node.getRight())!!.let { rightMin ->
                        node.setKey(rightMin.getKey())
                            .setValue(rightMin.getValue())
                            .setRight(doRemove(node.getRight(), rightMin.getKey()))
                    }
                }
            }
        }
        return node.updateHeight()
    }

    override fun clear() {
        this.root = null
        this.count = 0
    }
}

internal class BasicBSTNode<K : Comparable<K>, V> : BSTNode<K, V> {
    private var height: Int
    private var key: K
    private var value: V
    private var left: BSTNode<K, V>?
    private var right: BSTNode<K, V>?

    internal constructor(key: K, value: V) {
        this.key = key
        this.value = value
        this.height = DEFAULT_HEIGHT
        this.left = null
        this.right = null
    }

    override fun getHeight(): Int = height

    override fun setHeight(height: Int): BSTNode<K, V> {
        this.height = height
        return this
    }

    override fun getKey(): K = this.key

    override fun setKey(key: K): BSTNode<K, V> = this.apply {
        this.key = key
    }

    override fun getValue(): V = this.value

    override fun setValue(value: V): BSTNode<K, V> = this.apply {
        this.value = value
    }

    override fun getLeft(): BSTNode<K, V>? = this.left

    override fun setLeft(left: BSTNode<K, V>?): BSTNode<K, V> = this.apply {
        this.left = left
    }

    override fun getRight(): BSTNode<K, V>? = this.right

    override fun setRight(right: BSTNode<K, V>?): BSTNode<K, V> = this.apply {
        this.right = right
    }

    override fun toString(): String {
        return "BSTNode(key=$key, value=$value)"
    }

}
