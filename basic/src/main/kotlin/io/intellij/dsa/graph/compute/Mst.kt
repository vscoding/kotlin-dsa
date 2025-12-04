package io.intellij.dsa.graph.compute

import io.intellij.dsa.graph.Edge
import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.GraphChecker
import io.intellij.dsa.graph.Vertex
import io.intellij.dsa.uf.IndexedUnionFind
import io.intellij.dsa.uf.UnionFind
import java.util.*

/**
 * Mst 最小生成树
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class Mst(graph: Graph) : GraphChecker(graph) {

    init {
        checkEmpty().checkDirected(false).checkWeighted(true)
        require(Components(graph).compute().componentCount == 1) {
            "The graph must be connected."
        }
    }

    /**
     * Lazy Prim算法：懒惰删除 + 最小堆 + 切分
     */
    fun lazyPrim(): Result {
        val result = Result()
        val visited = BooleanArray(graph.getVertexesNum())

        val start = graph.vertexIndex().getVertex(0)!!
        val minHeap = PriorityQueue<Edge>(compareBy { it.weight })

        lazyPrim(start, visited, minHeap, result)
        return result
    }

    /**
     * Prim算法的递归实现：深度遍历 + 切分
     */
    private fun lazyPrim(vertex: Vertex, visited: BooleanArray, minHeap: PriorityQueue<Edge>, result: Result) {
        visited[vertex.id] = true
        minHeap.addAll(graph.adjacentEdges(vertex.id))

        while (minHeap.isNotEmpty()) {
            val min = minHeap.poll()!!
            val toV = min.to

            if (!visited[toV.id]) {
                visited[toV.id] = true
                result.edges.add(min)
                result.totalWeight += min.weight
                // 形成切分，继续递归
                lazyPrim(toV, visited, minHeap, result)
            }
        }
    }

    /**
     * Kruskal算法：最小堆 + 并查集 + 切分
     */
    fun kruskal(): Result {
        val result = Result()
        val vertexUF: UnionFind<Vertex> = IndexedUnionFind(Vertex::id)

        val edges = graph.getEdges()
        val queue = PriorityQueue<Edge>(compareBy { it.weight })
        queue.addAll(edges)

        while (queue.isNotEmpty()) {
            val minE = queue.poll()
            val fromV = minE.from
            val toV = minE.to

            vertexUF.add(fromV)
            vertexUF.add(toV)

            if (!vertexUF.isConnected(fromV, toV)) {
                // 说明不在同一个集合中
                result.edges.add(minE)
                result.totalWeight += minE.weight
                vertexUF.union(fromV, toV)
            }
        }
        return result
    }

    /**
     * MST计算结果
     */
    data class Result(
        val edges: MutableList<Edge> = mutableListOf(),
        var totalWeight: Double = 0.0
    ) {

        /**
         * 获取边的数量
         */
        fun getEdgeCount(): Int = edges.size

        /**
         * 检查是否形成了连通的MST
         */
        fun isConnected(vertexCount: Int): Boolean = edges.size == vertexCount - 1

        /**
         * 获取所有边的权重列表
         */
        fun getWeights(): List<Double> = edges.map { it.weight }

        /**
         * 打印MST信息
         */
        fun printMst() {
            println("=== Minimum Spanning Tree ===")
            println("Total Weight: $totalWeight")
            println("Edge Count  : ${edges.size}")
            println("Edges:")
            edges.forEachIndexed { index, edge ->
                println("  ${index + 1}. ${edge.from.name} -> ${edge.to.name}, weight: ${edge.weight}")
            }
        }

        override fun toString(): String {
            return "MST(edges=${edges.size}, totalWeight=$totalWeight)"
        }
    }

}