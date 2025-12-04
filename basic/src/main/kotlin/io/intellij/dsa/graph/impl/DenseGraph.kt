package io.intellij.dsa.graph.impl

import io.intellij.dsa.beautify
import io.intellij.dsa.getLogger
import io.intellij.dsa.graph.Edge
import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.VertexIndex

/**
 * DenseGraph 稠密图，邻接矩阵
 *
 * @author tech@intellij.io
 * @since 2025-05-29
 */
class DenseGraph(
    private val directed: Boolean,
    private val weighted: Boolean
) : Graph {

    companion object {
        val log = getLogger(DenseGraph::class.java)
    }

    // 邻接矩阵，使用 Double? 类型，null 表示无边
    private var adjacencyMatrix: Array<Array<Double?>>

    private val vertexIndex = VertexIndex()
    private var edgesCount: Int = 0

    init {
        adjacencyMatrix = Array(2) { Array(2) { null } }
    }

    override fun isDirected(): Boolean = this.directed

    override fun isWeighted(): Boolean = this.weighted

    override fun getEdgeNum(): Int = this.edgesCount

    override fun getEdges(): List<Edge> {
        return if (isEmpty()) {
            emptyList()
        } else {
            val edges = mutableListOf<Edge>()
            for (i in 0 until adjacencyMatrix.size) {
                for (j in 0 until adjacencyMatrix.size) {
                    adjacencyMatrix[i][j]?.let { weight ->
                        val fromV = vertexIndex.getVertex(i)!!
                        val toV = vertexIndex.getVertex(j)!!
                        edges.add(Edge(from = fromV, to = toV, weight = weight))
                    }
                }
            }
            edges
        }
    }

    override fun getEdge(from: Int, to: Int): Edge? {
        if (from < 0 || to < 0 || from >= vertexIndex.size() || to >= vertexIndex.size()) {
            return null
        }

        if (from == to) {
            return null // No self-loops in this implementation
        }

        val weight = adjacencyMatrix[from][to]
        return if (weight == null) {
            null
        } else {
            Edge(
                from = vertexIndex.getVertex(from)!!,
                to = vertexIndex.getVertex(to)!!,
                weight = weight
            )
        }
    }

    override fun connect(from: String, to: String, weight: Double) {
        require(from.isNotBlank() && to.isNotBlank()) {
            "Vertex names cannot be empty"
        }

        if (from == to) {
            return
        }
        val fromV = vertexIndex.createVertex(from)
        val toV = vertexIndex.createVertex(to)

        this.expand(vertexIndex.size())
        this.connect(fromV.id, toV.id, weight, directed = this.directed)
    }

    private fun expand(newSize: Int) {
        if (newSize > adjacencyMatrix.size) {
            val newMatrix = Array(newSize) { Array<Double?>(newSize) { null } }
            adjacencyMatrix.forEachIndexed { i, row ->
                System.arraycopy(row, 0, newMatrix[i], 0, row.size)
            }
            adjacencyMatrix = newMatrix
        }
    }

    private fun connect(from: Int, to: Int, weight: Double, directed: Boolean) {
        adjacencyMatrix[from][to]?.let { existingWeight ->
            log.debug("Edge from $from to $to already exists with weight $existingWeight, updating to $weight")
            edgesCount--
        }
        adjacencyMatrix[from][to] = weight
        edgesCount++
        if (!directed) {
            this.connect(to, from, weight, true)
        }
    }

    override fun adjacentEdges(id: Int): List<Edge> {
        if (isEmpty() || id < 0 || id >= vertexIndex.size()) {
            return emptyList()
        }
        val fromV = vertexIndex.getVertex(id)!!
        return adjacencyMatrix[id].mapIndexedNotNull { toIndex, weight ->
            weight?.let {
                val toV = vertexIndex.getVertex(toIndex)!!
                Edge(from = fromV, to = toV, weight = it)
            }
        }
    }

    override fun clear() {
        vertexIndex.clear()
        edgesCount = 0
        adjacencyMatrix = Array(2) { Array(2) { null } }
    }

    override fun showGraph() {
        println("Graph: ${if (directed) "Directed" else "Undirected"}, ${if (weighted) "Weighted" else "Unweighted"}")
        println("Vertices: ${vertexIndex.size()}")
        println("Edges: $edgesCount") //

        // 先打印顶点信息
        println("Vertex Information:")
        vertexIndex.getVertexes().forEach(::println)

        // 打印 matrix 并在第一行和第一列显示顶点信息
        println("\nAdjacency Matrix:")

        // 打印顶点ID作为列标题
        print("      ") // 留出行标题的空间
        for (i in 0 until vertexIndex.size()) {
            val v = vertexIndex.getVertex(i)!!
            print(beautify("${v.id}:${v.name}", 5))
        }
        println()

        // 打印矩阵内容
        for (i in 0 until vertexIndex.size()) {
            val v = vertexIndex.getVertex(i)!!
            print(beautify("${v.id}:${v.name}", 5))

            for (j in 0 until vertexIndex.size()) {
                val element = adjacencyMatrix[i][j]
                if (element != null) {
                    print(beautify(element.toString(), 5))
                } else {
                    print(beautify("nil", 5))
                }
            }
            println()
        }
    }

    override fun vertexIndex(): VertexIndex = this.vertexIndex

    override fun getAdjacencyMatrix(): Array<Array<Double?>>? = this.adjacencyMatrix

}
