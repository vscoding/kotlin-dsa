package io.intellij.dsa.graph

import io.intellij.dsa.graph.GraphUtils.buildGraph
import io.intellij.dsa.graph.compute.Components
import io.intellij.dsa.graph.compute.CycleAnalyzer
import io.intellij.dsa.graph.compute.Dijkstra
import io.intellij.dsa.graph.compute.Mst
import io.intellij.dsa.graph.compute.TopoSort
import io.intellij.dsa.graph.compute.Traverse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * GraphAlgorithmTest
 *
 * @author tech@intellij.io
 * @since 2025-05-31
 */
class GraphAlgorithmTest {

    private val traverseGraphText = """
        A B 1
        B C 2
        C D 1
        A E 1
        E F 1
        F G 1
        """.trimIndent()

    @Test
    fun `test graph traverse dfs`() {
        Traverse(
            buildGraph(traverseGraphText, directed = false, weighted = true),
            { println("Vertex: ${it.name}") },
            { println("Edge: ${it.from.name} -> ${it.to.name}, weight: ${it.weight}") }
        ).dfs()
    }

    @Test
    fun `test graph traverse bfs`() {
        Traverse(
            buildGraph(traverseGraphText, directed = false, weighted = true),
            { println("Vertex: ${it.name}") },
            { println("Edge: ${it.from.name} -> ${it.to.name}, weight: ${it.weight}") }
        ).bfs()
    }

    @Test
    fun `test graph components`() {
        val result = Components(
            buildGraph(
                """)
                A B 1
                B C 1
                A C 1
                D E 1
                E F 1
                F G 1
            """.trimIndent(), directed = false, weighted = true
            )
        ).compute()

        println("Component Count: ${result.componentCount}")

        Assertions.assertTrue(result.hasPath("A", "C"))
        Assertions.assertFalse(result.hasPath("A", "G"))
    }

    val mstGraphText = """
            0 1 4
            0 5 8
            1 5 11
            1 2 8
            5 6 7
            2 6 2
            5 4 8
            4 6 4
            2 3 3
            4 3 3
            """.trimIndent()

    @Test
    fun `test graph mst lazy prim`() {
        Mst(buildGraph(mstGraphText, directed = false, weighted = true))
            .lazyPrim().printMst()
    }

    @Test
    fun `test graph mst kruskal`() {
        Mst(buildGraph(mstGraphText, directed = false, weighted = true))
            .kruskal().printMst()
    }

    @Test
    fun `test graph topo sort kahn`() {
        val sort = TopoSort(
            buildGraph(
                """
                        0 1 1
                        0 5 1
                        0 6 1
                        2 0 1
                        2 3 1
                        3 5 1
                        5 4 1
                        6 4 1
                        7 6 1
                        8 7 1
                        6 9 1
                        9 10 1
                        9 11 1
                        9 12 1
                        11 12 1
        """.trimIndent(), directed = true, weighted = false
            )
        ).kahn()

        sort.printTopoSort()

    }

    @Test
    fun `test graph dijkstra`() {
        val graph = """
            A B 3
            A C 1
            B D 3
            C B 1
            C D 5
            C E 2
            D F 2
            E F 1
            B F 8
            """.trimIndent()

        val result = Dijkstra(buildGraph(graph, directed = true, weighted = true))
            .compute("A", null)

        listOf("B", "C", "D", "E", "F").map { v ->
            result.getRoutes(v)
        }.forEach { route ->
            result.printRoutes(route)
        }

    }


    @Test
    fun `test graph cycle analyzer shared point`() {
        val analysis = CycleAnalyzer(
            buildGraph(
                """
            A B 1
            B C 1
            C A 1
            C D 1
            D E 1
            E C 1
            """.trimIndent(), directed = true, weighted = true
            )
        ).findCycles()

        analysis.printCycles()

        Assertions.assertEquals(2, analysis.cycles.size)
    }

    @Test
    fun `test graph cycle analyzer shared edge`() {
        val analysis = CycleAnalyzer(
            buildGraph(
                """
            A B 1
            B C 1
            C D 1
            D A 1
            B D 1
            """.trimIndent(), directed = true, weighted = true
            )
        ).findCycles()

        analysis.printCycles()

        Assertions.assertEquals(2, analysis.cycles.size)
    }

}
