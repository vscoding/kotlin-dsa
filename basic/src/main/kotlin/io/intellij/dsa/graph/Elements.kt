package io.intellij.dsa.graph

import java.util.TreeMap

/**
 * 顶点
 */
data class Vertex(val name: String, val id: Int) {
    override fun toString(): String {
        return "V(id=$id, name='$name')"
    }
}

/**
 * 边
 */
data class Edge(val from: Vertex, val to: Vertex, var weight: Double = 1.0) {
    override fun toString(): String {
        return "E(from=${from.name}, to=${to.name}, weight=$weight)"
    }
}

/**
 * 维护 id 和 name 的索引
 */
class VertexIndex {
    // 顶点的表
    private val vertexes: MutableList<Vertex> = mutableListOf()
    private val namedVertexes = TreeMap<String, Vertex>()

    fun isEmpty(): Boolean = vertexes.isEmpty()

    fun size(): Int = vertexes.size

    fun getVertex(id: Int): Vertex? {
        return if (vertexes.isEmpty() || id < 0 || id >= vertexes.size) {
            null
        } else {
            vertexes[id]
        }
    }

    fun getVertexes(): List<Vertex> = vertexes

    fun getVertex(name: String): Vertex? {
        return namedVertexes[name]
    }

    fun createVertex(name: String): Vertex {
        return namedVertexes[name] ?: run {
            Vertex(name, vertexes.size).apply {
                vertexes.add(this)
                namedVertexes[name] = this
            }
        }
    }

    fun clear() {
        vertexes.clear()
        namedVertexes.clear()
    }

}
