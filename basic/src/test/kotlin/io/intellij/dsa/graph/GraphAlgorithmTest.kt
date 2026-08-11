package io.intellij.dsa.graph

import io.intellij.dsa.graph.compute.Components
import io.intellij.dsa.graph.compute.CycleAnalyzer
import io.intellij.dsa.graph.compute.Dijkstra
import io.intellij.dsa.graph.compute.Mst
import io.intellij.dsa.graph.compute.TopoSort
import io.intellij.dsa.graph.compute.Traverse
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * GraphAlgorithmTest
 *
 * @author dev@intellij.io
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
  fun `depth first traversal visits every vertex once`() {
    val visited = mutableListOf<String>()
    Traverse(
      buildGraph(traverseGraphText, directed = false, weighted = true, type = GraphType.SPARSE),
      {
        visited.add(it.name)
        println("Vertex: ${it.name}")
      },
      { println("Edge: ${it.from.name} -> ${it.to.name}, weight: ${it.weight}") },
    ).dfs()

    assertEquals(setOf("A", "B", "C", "D", "E", "F", "G"), visited.toSet())
    assertEquals(visited.size, visited.distinct().size)
  }

  @Test
  fun `breadth first traversal visits every vertex once`() {
    val visited = mutableListOf<String>()
    Traverse(
      buildGraph(traverseGraphText, directed = false, weighted = true, type = GraphType.SPARSE),
      {
        visited.add(it.name)
        println("Vertex: ${it.name}")
      },
      { println("Edge: ${it.from.name} -> ${it.to.name}, weight: ${it.weight}") },
    ).bfs()

    assertEquals(setOf("A", "B", "C", "D", "E", "F", "G"), visited.toSet())
    assertEquals(visited.size, visited.distinct().size)
  }

  @Test
  fun `components distinguish connected and disconnected vertices`() {
    val result = Components(
      buildGraph(
        """)
                A B 1
                B C 1
                A C 1
                D E 1
                E F 1
                F G 1
            """.trimIndent(),
        directed = false, weighted = true, type = GraphType.SPARSE,
      ),
    ).compute()

    println("Component Count: ${result.componentCount}")

    assertEquals(2, result.componentCount)
    assertTrue(result.hasPath("A", "C"))
    assertFalse(result.hasPath("A", "G"))
  }

  private val mstGraphText = """
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
  fun `lazy prim builds the expected minimum spanning tree`() {
    val graph = buildGraph(mstGraphText, directed = false, weighted = true, type = GraphType.SPARSE)
    val result = Mst(graph).lazyPrim()

    result.printMst()

    assertEquals(6, result.getEdgeCount())
    assertTrue(result.isConnected(graph.getVertexesNum()))
    assertEquals(27.0, result.totalWeight)
  }

  @Test
  fun `kruskal builds the expected minimum spanning tree`() {
    val graph = buildGraph(mstGraphText, directed = false, weighted = true, type = GraphType.SPARSE)
    val result = Mst(graph).kruskal()

    result.printMst()

    assertEquals(6, result.getEdgeCount())
    assertTrue(result.isConnected(graph.getVertexesNum()))
    assertEquals(27.0, result.totalWeight)
  }

  @Test
  fun `kahn places every source before its destinations`() {
    val graph = buildGraph(
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
      """.trimIndent(),
      directed = true, weighted = false, type = GraphType.SPARSE,
    )
    val sort = TopoSort(graph).kahn()

    sort.printTopoSort()

    assertTrue(sort.isValid(graph.getVertexesNum()))
    graph.getEdges().forEach { edge ->
      assertTrue(sort.getPosition(edge.from.name) < sort.getPosition(edge.to.name))
    }
  }

  @Test
  fun `dijkstra computes shortest routes from the source`() {
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

    val result = Dijkstra(buildGraph(graph, directed = true, weighted = true, type = GraphType.SPARSE))
      .compute("A", null)

    listOf("B", "C", "D", "E", "F").map { v ->
      result.getRoutes(v)
    }.forEach { route ->
      result.printRoutes(route)
    }

    assertEquals(
      mapOf("A" to 0.0, "B" to 2.0, "C" to 1.0, "D" to 5.0, "E" to 3.0, "F" to 4.0),
      result.getAllDistances(),
    )
    assertEquals(listOf("A-C", "C-E", "E-F"), result.getRoutes("F").map { "${it.from.name}-${it.to.name}" })
  }

  @Test
  fun `cycle analyzer finds cycles that share a vertex`() {
    val analysis = CycleAnalyzer(
      buildGraph(
        """
            A B 1
            B C 1
            C A 1
            C D 1
            D E 1
            E C 1
            """.trimIndent(),
        directed = true, weighted = true, type = GraphType.SPARSE,
      ),
    ).findCycles()

    analysis.printCycles()

    assertEquals(2, analysis.cycles.size)
    assertEquals(
      setOf(setOf("A", "B", "C"), setOf("C", "D", "E")),
      analysis.cycles.map { cycle -> cycle.map { it.name }.toSet() }.toSet(),
    )
  }

  @Test
  fun `cycle analyzer finds cycles that share an edge`() {
    val analysis = CycleAnalyzer(
      buildGraph(
        """
            A B 1
            B C 1
            C D 1
            D A 1
            B D 1
            """.trimIndent(),
        directed = true, weighted = true, type = GraphType.SPARSE,
      ),
    ).findCycles()

    analysis.printCycles()

    assertEquals(2, analysis.cycles.size)
    assertEquals(
      setOf(setOf("A", "B", "C", "D"), setOf("A", "B", "D")),
      analysis.cycles.map { cycle -> cycle.map { it.name }.toSet() }.toSet(),
    )
  }

}
