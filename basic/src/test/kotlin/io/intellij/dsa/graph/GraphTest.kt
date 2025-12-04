package io.intellij.dsa.graph

import io.intellij.dsa.graph.GraphUtils.buildGraph
import org.junit.jupiter.api.Test

/**
 * GraphTest
 *
 * @author zhuzhenjie
 */
class GraphTest {

    val graphText = """
        A B 1
        B C 2
        C D 3
        B D 4
    """.trimIndent()

    @Test
    fun `test show dense graph`() {
        buildGraph(graphText, directed = false, weighted = true, type = GraphUtils.GraphType.DENSE)
            .showGraph()
    }

    @Test
    fun `test show sparse graph`() {
        buildGraph(graphText, directed = true, weighted = true, type = GraphUtils.GraphType.SPARSE)
            .showGraph()
    }

}
