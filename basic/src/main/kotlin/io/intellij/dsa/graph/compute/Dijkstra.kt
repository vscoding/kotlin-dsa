package io.intellij.dsa.graph.compute

import io.intellij.dsa.graph.DEFAULT_UNWEIGHTED_VALUE
import io.intellij.dsa.graph.Edge
import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.GraphChecker
import io.intellij.dsa.graph.Vertex
import java.util.*

/**
 * Dijkstra 最短路径算法
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class Dijkstra(graph: Graph) : GraphChecker(graph) {

    init {
        checkEmpty()
    }

    /**
     * 计算从源点到所有其他点的最短路径
     */
    fun compute(source: String): Result {
        return compute(source, null)
    }

    /**
     * 计算最短路径，支持断点过滤
     * @param source 源点名称
     * @param brokenFilter 需要过滤的断点集合
     */
    fun compute(source: String, brokenFilter: Set<String>?): Result {
        val sourceV = checkVertex(source, true)

        // 处理断点过滤器
        val validBrokenFilter = brokenFilter?.let { filter ->
            filter.map { name -> checkVertex(name, false) }
                .map { it.name }
                .filter { it != sourceV.name }
                .toMutableSet()
                .takeIf { it.isNotEmpty() }
        }

        val record = Result(sourceV, graph)

        // 初始化最小堆：局部最优更新到全局最优
        val minHeap = graph.adjacentEdges(sourceV.id).map { edge ->
            val weight = if (graph.isWeighted()) edge.weight else DEFAULT_UNWEIGHTED_VALUE
            TotalWeight(edge, weight).also { tw ->
                val otherVertexId = tw.edge.to.id
                record.dynamicDistanceToSource[otherVertexId] = tw.totalWeight
                record.dynamicPathFrom[otherVertexId] = tw.edge
            }
        }.let { list ->
            PriorityQueue<TotalWeight>(compareBy { it.totalWeight }).apply {
                addAll(list)
            }
        }

        // 设置源点
        record.calculateCompleted[sourceV.id] = true
        record.dynamicDistanceToSource[sourceV.id] = 0.0
        record.dynamicPathFrom[sourceV.id] = null

        // Dijkstra 主循环
        while (minHeap.isNotEmpty()) {
            val min = minHeap.poll()
            if (!compute(min, minHeap, record, validBrokenFilter)) {
                break
            }
        }
        return record
    }

    /**
     * 单步计算逻辑
     */
    private fun compute(
        min: TotalWeight,
        minHeap: PriorityQueue<TotalWeight>,
        record: Result,
        brokenFilter: MutableSet<String>?
    ): Boolean {
        val completed = record.calculateCompleted
        val dts = record.dynamicDistanceToSource
        val pathFrom = record.dynamicPathFrom

        val pivotVertex = min.edge.to
        if (completed[pivotVertex.id]) {
            return true
        }

        val toW = dts[pivotVertex.id]!!
        val updatedEdges = graph.adjacentEdges(pivotVertex.id)

        updatedEdges.forEach { updatedEdge ->
            val toto = updatedEdge.to
            if (completed[toto.id]) {
                return@forEach
            }
            val updatedWeight = updatedEdge.weight + toW
            val totoDis = dts[toto.id]
            when {
                totoDis == null -> {
                    // 没有到达过
                    dts[toto.id] = updatedWeight
                    pathFrom[toto.id] = updatedEdge
                    minHeap.add(TotalWeight(updatedEdge, updatedWeight))
                }

                updatedWeight < totoDis -> {
                    // 更新最短路径
                    dts[toto.id] = updatedWeight
                    pathFrom[toto.id] = updatedEdge
                    minHeap.add(TotalWeight(updatedEdge, updatedWeight))
                }
            }
        }

        completed[pivotVertex.id] = true
        return !canBeBroken(brokenFilter, pivotVertex.name)
    }

    /**
     * 检查是否可以提前终止（所有断点都已处理）
     */
    private fun canBeBroken(breakFilter: MutableSet<String>?, complete: String): Boolean {
        if (breakFilter == null) {
            return false
        }
        breakFilter.remove(complete)
        return breakFilter.isEmpty()
    }

    /**
     * 总权重数据类
     */
    private data class TotalWeight(val edge: Edge, val totalWeight: Double)

    /**
     * Dijkstra算法计算结果
     */
    class Result internal constructor(
        private val source: Vertex,
        private val graph: Graph
    ) {
        internal val calculateCompleted = BooleanArray(graph.getVertexesNum())
        internal val dynamicDistanceToSource = arrayOfNulls<Double>(graph.getVertexesNum())
        internal val dynamicPathFrom = arrayOfNulls<Edge>(graph.getVertexesNum())

        /**
         * 获取到目标点的最短路径
         */
        fun getRoutes(destName: String): List<Edge> {
            var destV = graph.vertexIndex().getVertex(destName) ?: return emptyList()

            if (destV.id >= dynamicPathFrom.size) {
                return emptyList()
            }

            val reversedRoutes = mutableListOf<Edge>()
            while (source.name != destV.name) {
                val edge = dynamicPathFrom[destV.id] ?: break
                reversedRoutes.add(edge)
                destV = edge.from
            }

            return reversedRoutes.reversed()
        }

        /**
         * 获取到目标点的最短距离
         */
        fun getDistance(destName: String): Double? {
            val destV = graph.vertexIndex().getVertex(destName) ?: return null
            return dynamicDistanceToSource[destV.id]
        }

        /**
         * 检查是否有到目标点的路径
         */
        fun hasPath(destName: String): Boolean {
            return getDistance(destName) != null
        }

        /**
         * 获取所有可达点的最短距离
         */
        fun getAllDistances(): Map<String, Double> {
            return buildMap {
                graph.getVertexes().forEach { vertex ->
                    dynamicDistanceToSource[vertex.id]?.let { distance ->
                        put(vertex.name, distance)
                    }
                }
            }
        }

        /**
         * 打印路径信息
         */
        fun printRoutes(edges: List<Edge>) {
            if (edges.isEmpty()) {
                println("No route found")
                return
            }

            val lastEdge = edges.last()
            val target = lastEdge.to.name
            val distance = dynamicDistanceToSource[lastEdge.to.id]

            println(
                """
                Shortest Path:
                  source: [${source.name}] target: [$target]
                """.trimIndent()
            )

            println("Distance: $distance = ${edges.joinToString(" + ") { it.weight.toString() }}")

            print("Route: ")
            edges.forEachIndexed { index, edge ->
                val from = edge.from.name
                val weight = edge.weight

                when (index) {
                    edges.size - 1 -> {
                        val to = edge.to.name
                        println("[$from] --$weight-> [$to]")
                    }

                    else -> print("[$from] --$weight-> ")
                }
            }
            println()
        }

        /**
         * 打印所有最短路径
         */
        fun printAllRoutes() {
            println("=== Dijkstra Shortest Paths from [${source.name}] ===")
            graph.getVertexes()
                .filter { it.name != source.name }
                .forEach { vertex ->
                    val routes = getRoutes(vertex.name)
                    if (routes.isNotEmpty()) {
                        printRoutes(routes)
                    } else {
                        println("No path to [${vertex.name}]")
                    }
                }
        }
    }

}