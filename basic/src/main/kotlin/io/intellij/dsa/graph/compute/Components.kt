package io.intellij.dsa.graph.compute

import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.GraphChecker
import io.intellij.dsa.graph.Vertex
import io.intellij.dsa.uf.IndexedUnionFind
import io.intellij.dsa.uf.UnionFind

/**
 * Components 无向图连通分量
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class Components(graph: Graph) : GraphChecker(graph) {

    init {
        checkEmpty().checkDirected(false)
    }

    fun compute(): Result {
        val result = Result(graph)
        val visited = BooleanArray(graph.getVertexesNum())
        var count = 0

        graph.getVertexes().forEach { v ->
            if (!visited[v.id]) {
                this.dfs(v, visited, result)
                count++
                result.setComponentCount(count)
            }
        }
        return result
    }

    private fun dfs(vertex: Vertex, visited: BooleanArray, result: Result) {
        visited[vertex.id] = true

        this.graph.adjacentEdges(vertex.id).forEach { edge ->
            val toV = edge.to
            if (visited[toV.id]) return@forEach

            // Union-Find 合并操作
            result.uf.union(vertex, toV)

            // 继续深度优先遍历
            this.dfs(toV, visited, result)
        }
    }

    class Result(private val graph: Graph) {

        private var _count = 0

        val componentCount: Int
            get() = _count

        internal val uf: UnionFind<Vertex> = IndexedUnionFind(Vertex::id)

        internal fun setComponentCount(count: Int) {
            this._count = count
        }

        fun hasPath(src: String, dest: String): Boolean {
            val srcV = graph.vertexIndex().getVertex(src)
            val destV = graph.vertexIndex().getVertex(dest)
            if (srcV == null || destV == null) return false
            return uf.isConnected(srcV, destV)
        }

    }

}