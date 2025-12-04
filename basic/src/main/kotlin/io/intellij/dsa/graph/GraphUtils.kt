package io.intellij.dsa.graph

import io.intellij.dsa.graph.impl.DenseGraph
import io.intellij.dsa.graph.impl.SparseGraph

object GraphUtils {

    enum class GraphType {
        DENSE, SPARSE
    }

    fun buildGraph(graphText: String, directed: Boolean, weighted: Boolean, type: GraphType): Graph {
        val graph = when (type) {
            GraphType.DENSE -> DenseGraph(directed, weighted)
            GraphType.SPARSE -> SparseGraph(directed, weighted)
        }
        return graph.apply {
            graphText.textToLines().forEach { line ->
                line.lineToEdge("\\s+")?.let {
                    val weight = it.weight
                    this.connect(from = it.from.name, to = it.to.name, weight = weight)
                }
            }
        }
    }

    fun buildGraph(graphText: String, directed: Boolean, weighted: Boolean): Graph {
        val type = if (Math.random() < 0.5) GraphType.DENSE else GraphType.SPARSE
        println("build graph with type: $type")
        return buildGraph(
            graphText, directed, weighted, type
        )
    }

    internal fun String.textToLines(): List<String> {
        return this.lines().map { it.trim() }.filter { it.isNotEmpty() }
    }

    internal fun String.lineToEdge(regDelimiter: String): Edge? {
        val parts = this.trim().split(Regex(regDelimiter))
        if (parts.size < 2) return null

        val from = parts[0].trim()
        val to = parts[1].trim()

        // 验证顶点名称不为空
        if (from.isEmpty() || to.isEmpty()) return null

        val weight = if (parts.size > 2) {
            parts[2].trim().toDoubleOrNull() ?: DEFAULT_UNWEIGHTED_VALUE
        } else {
            DEFAULT_UNWEIGHTED_VALUE
        }

        return Edge(
            from = Vertex(from, -1),
            to = Vertex(to, -1),
            weight = weight
        )

    }
}
