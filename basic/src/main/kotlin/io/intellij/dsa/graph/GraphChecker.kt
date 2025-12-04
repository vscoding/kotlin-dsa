package io.intellij.dsa.graph

/**
 * GraphChecker
 *
 * @author tech@intellij.io
 */
abstract class GraphChecker(
    protected val graph: Graph
) {

    fun checkEmpty(): GraphChecker {
        require(!graph.isEmpty()) {
            "Graph is empty"
        }
        return this
    }

    fun checkDirected(expectedDirected: Boolean): GraphChecker {
        require(graph.isDirected() == expectedDirected) {
            "Graph is not ${if (expectedDirected) "directed" else "undirected"}"
        }
        return this
    }

    fun checkWeighted(expectedWeighted: Boolean): GraphChecker {
        require(graph.isWeighted() == expectedWeighted) {
            "Graph is not ${if (expectedWeighted) "weighted" else "unweighted"}"
        }
        return this
    }

    fun checkVertex(name: String, required: Boolean): Vertex {
        return graph.vertexIndex().getVertex(name) ?: if (required) {
            throw IllegalArgumentException("Vertex '$name' not found")
        } else {
            throw NoSuchElementException("Vertex '$name' not found")
        }
    }

}
