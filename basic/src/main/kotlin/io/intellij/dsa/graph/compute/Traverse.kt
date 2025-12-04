package io.intellij.dsa.graph.compute

import io.intellij.dsa.graph.Edge
import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.GraphChecker
import io.intellij.dsa.graph.Vertex
import java.util.TreeSet
import kotlin.collections.ArrayDeque

/**
 * Traverse 图遍历
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class Traverse(
    graph: Graph,
    private val vertexConsumer: (Vertex) -> Unit,
    private val edgeConsumer: (Edge) -> Unit
) : GraphChecker(graph) {

    init {
        checkEmpty()
    }

    fun dfs() {
        val visited = TreeSet<Int>()
        this.graph.getVertexes().forEach {
            if (!visited.contains(it.id)) {
                this.dfs(it, vertexConsumer, edgeConsumer, visited)
            }
        }
    }

    // 深度优先遍历
    private fun dfs(vertex: Vertex, vc: (Vertex) -> Unit, ec: (Edge) -> Unit, visited: TreeSet<Int>) {
        visited.add(vertex.id)
        vc(vertex)
        this.graph.adjacentEdges(vertex.id).forEach {
            val toV = it.to
            if (visited.contains(toV.id)) return@forEach
            ec(it)
            this.dfs(toV, vc, ec, visited)
        }
    }

    // 广度优先遍历
    fun bfs() {
        val visited = TreeSet<Int>()
        this.graph.getVertexes().forEach {
            if (!visited.contains(it.id)) {
                this.bfs(it, vertexConsumer, edgeConsumer, visited)
            }
        }
    }

    private fun bfs(vertex: Vertex, vc: (Vertex) -> Unit, ec: (Edge) -> Unit, visited: TreeSet<Int>) {
        val queue = ArrayDeque<Vertex>().apply {
            add(vertex)
        }
        while (queue.isNotEmpty()) {
            queue.removeFirst().let { v ->
                visited.add(v.id)
                vc(v)
                this.graph.adjacentEdges(v.id).forEach { e ->
                    val toV = e.to
                    if (visited.contains(toV.id)) return@forEach
                    ec(e)
                    queue.addLast(toV)
                }
            }
        }
    }

}
