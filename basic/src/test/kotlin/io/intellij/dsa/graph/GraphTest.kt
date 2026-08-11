package io.intellij.dsa.graph

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * GraphTest
 *
 * @author zhuzhenjie
 */
class GraphTest {

  private val graphText = """
        A B 1
        B C 2
        C D 3
        B D 4
    """.trimIndent()

  @Test
  fun `dense graph stores an undirected weighted graph`() {
    val graph = buildGraph(graphText, directed = false, weighted = true, type = GraphType.DENSE)

    assertFalse(graph.isDirected())
    assertTrue(graph.isWeighted())
    assertEquals(4, graph.getVertexesNum())
    assertEquals(8, graph.getEdgeNum())
    assertEquals(4.0, assertNotNull(graph.getEdge("B", "D")).weight)

    graph.showGraph()
  }

  @Test
  fun `sparse graph stores a directed weighted graph`() {
    val graph = buildGraph(graphText, directed = true, weighted = true, type = GraphType.SPARSE)

    assertTrue(graph.isDirected())
    assertTrue(graph.isWeighted())
    assertEquals(4, graph.getVertexesNum())
    assertEquals(4, graph.getEdgeNum())
    assertNotNull(graph.getEdge("B", "D"))
    assertEquals(null, graph.getEdge("D", "B"))

    graph.showGraph()
  }

}
