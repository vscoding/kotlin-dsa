package io.intellij.dsa.graph.compute

import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.GraphChecker
import io.intellij.dsa.graph.Vertex
import java.util.*

/**
 * TopoSort
 *
 * @author tech@intellij.io
 * @since 2025-06-01
 */
class TopoSort(graph: Graph) : GraphChecker(graph) {

    init {
        // 必须是有向无环图
        checkEmpty().checkDirected(true)
        val cycleAnalyzer = CycleAnalyzer(graph)
        require(!cycleAnalyzer.findCycles(true).hasCycle()) {
            "Graph contains cycles, cannot compute topological sort"
        }
    }

    /**
     * Kahn 算法实现拓扑排序
     */
    fun kahn(): Result {
        // 计算每个顶点的入度
        val inDegree = IntArray(graph.vertexIndex().size())
        graph.getEdges().forEach { edge ->
            inDegree[edge.to.id]++
        }

        // 找到所有入度为 0 的顶点
        val zeroDegreeVertices = graph.getVertexes()
            .filter { inDegree[it.id] == 0 }
            .map { VertexWrapper(0, it) }

        val result = Result()
        val queue = ArrayDeque(zeroDegreeVertices)
        val processedVertices = TreeSet<Int>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            processedVertices.add(current.vertex.id)
            result.add(current)

            // 处理当前顶点的所有邻接顶点
            graph.adjacentEdges(current.vertex.id).forEach { edge ->
                val neighbor = edge.to
                if (neighbor.id !in processedVertices) {
                    inDegree[neighbor.id]--
                    // 如果邻接顶点的入度变为 0，则加入队列
                    if (inDegree[neighbor.id] == 0) {
                        queue.offer(VertexWrapper(current.degree + 1, neighbor))
                    }
                }
            }
        }

        return result
    }

    /**
     * 顶点包装器，包含入度信息
     * @param degree 入度
     * @param vertex 顶点
     */
    data class VertexWrapper(val degree: Int, val vertex: Vertex) {
        override fun toString(): String {
            return "${vertex.name}($degree)"
        }
    }

    /**
     * 拓扑排序结果
     */
    class Result {
        private val _sorted = mutableListOf<VertexWrapper>()
        val sorted: List<VertexWrapper> get() = _sorted.toList()

        internal fun add(vertexWrapper: VertexWrapper) {
            _sorted.add(vertexWrapper)
        }

        /**
         * 打印拓扑排序结果
         */
        fun printTopoSort() {
            printTopoSort(sorted)
        }

        /**
         * 打印拓扑排序结果
         */
        private fun printTopoSort(sorted: List<VertexWrapper>) {
            if (sorted.isEmpty()) {
                println("No vertices in the graph")
                return
            }
            println("Topological Sort Result: ")
            println(sorted.joinToString(" -> ") { it.toString() })
        }

        /**
         * 检查拓扑排序是否有效（是否包含所有顶点）
         */
        fun isValid(totalVertices: Int): Boolean {
            return sorted.size == totalVertices
        }

        /**
         * 获取排序后的顶点名称列表
         */
        fun getVertexNames(): List<String> {
            return sorted.map { it.vertex.name }
        }

        /**
         * 获取指定顶点在拓扑排序中的位置
         */
        fun getPosition(vertexName: String): Int {
            return sorted.indexOfFirst { it.vertex.name == vertexName }
        }
    }

}
