package io.intellij.dsa.tree.bst

import io.intellij.dsa.KVOperator

interface BST<K : Comparable<K>, V> : KVOperator<K, V> {

    /**
     * Retrieves the root node of the binary search tree (BST).
     *
     * @return the root node of the BST, or null if the tree is empty
     */
    fun getRoot(): BSTNode<K, V>?


    /**
     * Determines whether the binary search tree (BST) contains the specified key.
     *
     * @param key the key to search for in the binary search tree
     * @return true if the key exists in the tree, false otherwise
     */
    override fun contains(key: K): Boolean {
        return getNode(key) != null
    }

    /**
     * Retrieves the value associated with the specified key in the binary search tree (BST).
     */
    override fun get(key: K): V? {
        return getNode(key)?.getValue()
    }

    /**
     * Retrieves the node associated with the specified key in the binary search tree (BST).
     *
     * @param key the key to search for in the binary search tree
     * @return the node corresponding to the given key, or null if the key is not found
     */
    fun getNode(key: K): BSTNode<K, V>? {
        return getNode(getRoot(), key)
    }

    /**
     * Determines whether the binary search tree (BST) satisfies the properties of a valid BST.
     *
     * @return true if the tree is a valid binary search tree, false otherwise
     */
    fun isBst(): Boolean {
        return isBST(node = getRoot())
    }

    /**
     * Retrieves the minimum node in the binary search tree (BST).
     *
     * @return the node containing the smallest key in the tree, or null if the tree is empty
     */
    fun getMin(): BSTNode<K, V>? {
        return getMin(getRoot())
    }

    /**
     * Retrieves the maximum node in the binary search tree (BST).
     *
     * @return the node containing the largest key in the tree, or null if the tree is empty
     */
    fun getMax(): BSTNode<K, V>? {
        return getMax(getRoot())
    }

    fun preorder(action: (BSTNode<K, V>) -> Unit) {
        getRoot()?.preorder(action)
    }

    fun inorder(action: (BSTNode<K, V>) -> Unit) {
        getRoot()?.inorder(action)
    }

    fun postorder(action: (BSTNode<K, V>) -> Unit) {
        getRoot()?.postorder(action)
    }

    fun bfs(action: (BSTNode<K, V>) -> Unit) {
        getRoot()?.bfs(action)
    }

    fun <K : Comparable<K>, V> BSTNode<K, V>.preorder(action: (BSTNode<K, V>) -> Unit) {
        action.apply { this }

        getLeft()?.preorder(action)
        getRight()?.preorder(action)
    }

    fun <K : Comparable<K>, V> BSTNode<K, V>.inorder(action: (BSTNode<K, V>) -> Unit) {
        getLeft()?.inorder(action)
        action.apply { this }
        getRight()?.inorder(action)
    }

    fun <K : Comparable<K>, V> BSTNode<K, V>.postorder(action: (BSTNode<K, V>) -> Unit) {
        getLeft()?.postorder(action)
        getRight()?.postorder(action)
        action.apply { this }
    }

    fun <K : Comparable<K>, V> BSTNode<K, V>.bfs(action: (BSTNode<K, V>) -> Unit) {
        val queue = ArrayDeque<BSTNode<K, V>>()
        queue.add(this)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            action(current)

            current.getLeft()?.let { queue.addLast(it) }
            current.getRight()?.let { queue.addLast(it) }
        }
    }

}
