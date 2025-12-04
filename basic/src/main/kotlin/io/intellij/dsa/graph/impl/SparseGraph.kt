package io.intellij.dsa.graph.impl

import io.intellij.dsa.getLogger
import io.intellij.dsa.graph.Edge
import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.VertexIndex
import java.util.TreeMap

/**
 * SparseGraph 稀疏图，使用邻接表实现
 *
 * @author tech@intellij.io
 * @since 2025-05-29
 */
class SparseGraph(
    private val directed: Boolean,
    private val weighted: Boolean
) : Graph {

    companion object {
        val log = getLogger(DenseGraph::class.java)
    }

    private val vertexIndex = VertexIndex()

    // 邻接表，使用 TreeMap 存储边，便于按键排序
    private var adjacencyList: Array<TreeMap<Int, Double>> = Array(0) { TreeMap() }

    private var edgesCount: Int = 0

    override fun isDirected(): Boolean = this.directed

    override fun isWeighted(): Boolean = this.weighted

    override fun getEdgeNum(): Int = this.edgesCount

    override fun getEdges(): List<Edge> {
        return adjacencyList.flatMapIndexed { from, edges ->
            if (edges.isEmpty()) {
                emptyList() // 返回空列表而不是 null
            } else {
                val fromV = vertexIndex.getVertex(from)!!
                edges.map { (toId, weight) ->
                    val toV = vertexIndex.getVertex(toId)!!
                    Edge(from = fromV, to = toV, weight = weight)
                }
            }
        }
    }

    override fun getEdge(from: Int, to: Int): Edge? {
        if (from < 0 || to < 0 || from >= vertexIndex.size() || to >= vertexIndex.size()) {
            return null
        }
        if (from == to) {
            return null
        }
        return adjacencyList[from].let {
            it[to]?.let { weight ->
                val fromVertex = vertexIndex.getVertex(from)!!
                val toVertex = vertexIndex.getVertex(to)!!
                Edge(from = fromVertex, to = toVertex, weight = weight)
            }
        }
    }

    override fun connect(from: String, to: String, weight: Double) {
        require(from.isNotBlank() && to.isNotBlank()) {
            "Vertex names cannot be empty"
        }

        if (from == to) {
            return
        }
        this.connect(
            vertexIndex.createVertex(from).id,
            vertexIndex.createVertex(to).id,
            weight,
            this.directed
        )
    }

    private fun connect(from: Int, to: Int, weight: Double, directed: Boolean) {
        this.expand(maxOf(from, to) + 1)

        val edges = adjacencyList[from]
        val isNewEdge = !edges.containsKey(to)

        if (!isNewEdge) {
            log.warn("Edge from $from to $to already exists, updating weight to $weight")
            this.edgesCount--
        }

        edges[to] = weight
        this.edgesCount++
        if (!directed) {
            this.connect(to, from, weight, true)
        }

    }

    private fun expand(size: Int) {
        if (size > adjacencyList.size) {
            val newAdjList = Array(size) { TreeMap<Int, Double>() }
            System.arraycopy(adjacencyList, 0, newAdjList, 0, adjacencyList.size)
            this.adjacencyList = newAdjList
        }
    }

    override fun adjacentEdges(id: Int): List<Edge> {
        if (id < 0 || id >= vertexIndex.size()) {
            return emptyList()
        }
        val fromVertex = vertexIndex.getVertex(id)!!
        val neighbors = adjacencyList[id]
        return neighbors.map { (toId, weight) ->
            val toVertex = vertexIndex.getVertex(toId) ?: return@map null
            Edge(from = fromVertex, to = toVertex, weight = weight)
        }.filterNotNull()
    }

    override fun clear() {
        vertexIndex.clear()
        adjacencyList = Array(0) { TreeMap() }
        edgesCount = 0
    }

    override fun showGraph() {
        println("Graph: ${if (directed) "Directed" else "Undirected"}, ${if (weighted) "Weighted" else "Unweighted"}")
        println("Vertices: ${vertexIndex.size()}")
        println("Edges: $edgesCount")

        // 打印邻接表
        println("\nAdjacency List:")
        val startFmt = "%s(%d) : "
        val toFmt = "%s(%d) -- %.2f -> %s(%d)   "

        adjacencyList.forEachIndexed { from, map ->
            if (map.isEmpty()) {
                return@forEachIndexed
            }
            val fromV = vertexIndex.getVertex(from)!!
            print(startFmt.format(fromV.name, fromV.id))
            map.forEach { (toId, weight) ->
                val toV = vertexIndex.getVertex(toId)!!
                print(toFmt.format(fromV.name, fromV.id, weight, toV.name, toId))
            }
            println()
        }

    }

    override fun vertexIndex(): VertexIndex = this.vertexIndex

    override fun getAdjacencyList(): Array<TreeMap<Int, Double>> = this.adjacencyList

}