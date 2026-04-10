package io.intellij.dsa.graph

import java.util.TreeMap

const val DEFAULT_UNWEIGHTED_VALUE = 1.0

/**
 * Graph
 *
 * @author tech@intellij.io
 * @since 2025-05-29
 */
interface Graph {

  fun isEmpty(): Boolean {
    return getVertexesNum() == 0
  }

  /**
   * Determines whether the graph is directed.
   *
   * @return true if the graph is directed, false otherwise
   */
  fun isDirected(): Boolean

  /**
   * Checks whether the graph is weighted.
   *
   * @return true if the graph is weighted, false otherwise
   */
  fun isWeighted(): Boolean

  /**
   * Retrieves the number of vertices in the graph.
   *
   * @return the total count of vertices in the graph
   */
  fun getVertexesNum(): Int = vertexIndex().size()

  /**
   * Retrieves the total number of edges in the graph.
   *
   * @return the total count of edges in the graph
   */
  fun getEdgeNum(): Int

  /**
   * Retrieves a list of all vertices in the graph.
   *
   * @return a list of Vertex objects representing all vertices in the graph
   */
  fun getVertexes(): List<Vertex> = vertexIndex().getVertexes()

  /**
   * Retrieves a list of all edges in the graph.
   *
   * @return a list of Edge objects representing all edges in the graph
   */
  fun getEdges(): List<Edge>

  /**
   * Retrieves an edge connecting the specified vertices by their names.
   *
   * @param from the name of the source vertex
   * @param to the name of the destination vertex
   * @return the Edge connecting the specified vertices, or null if no such edge exists or if either vertex is not found
   */
  fun getEdge(from: String, to: String): Edge? {
    val fromV = vertexIndex().getVertex(from)
    val toV = vertexIndex().getVertex(to)
    if (fromV == null || toV == null) {
      return null
    }
    return getEdge(fromV.id, toV.id)
  }

  /**
   * Retrieves an edge connecting two vertices specified by their IDs.
   *
   * @param from the ID of the source vertex
   * @param to the ID of the destination vertex
   * @return the Edge connecting the specified vertices, or null if no such edge exists
   */
  fun getEdge(from: Int, to: Int): Edge?

  /**
   * Connects two vertices in the graph with the default unweighted value.
   *
   * @param from the name of the source vertex
   * @param to the name of the destination vertex
   */
  fun connect(from: String, to: String) = connect(from, to, DEFAULT_UNWEIGHTED_VALUE)

  /**
   * Connects two vertices in the graph with a weighted edge.
   *
   * @param from the name of the source vertex
   * @param to the name of the destination vertex
   * @param weight the weight of the edge connecting the vertices
   */
  fun connect(from: String, to: String, weight: Double)

  fun adjacentEdges(name: String): List<Edge> {
    return vertexIndex().getVertex(name)?.let { vertex ->
      adjacentEdges(vertex.id)
    } ?: emptyList()
  }

  /**
   * Retrieves a list of edges that are adjacent to the vertex specified by its ID.
   *
   * @param id the ID of the vertex whose adjacent edges are to be retrieved
   * @return a list of Edge objects representing the edges adjacent to the specified vertex
   */
  fun adjacentEdges(id: Int): List<Edge>


  /**
   * Clears all data from the graph, including all vertices and edges.
   *
   * This method completely resets the graph to an empty state,
   * removing all connections and state information associated
   * with the graph.
   *
   * After calling this method, the graph will be empty, and any
   * operations dependent on existing vertices or edges will behave
   * as though the graph is newly initialized.
   */
  fun clear()

  /**
   * Displays the graph in a visual format.
   */
  fun showGraph()

  /**
   * Retrieves the VertexIndex associated with this graph.
   *
   * @return the VertexIndex instance used to maintain the mapping between vertex names and their corresponding indices
   */
  fun vertexIndex(): VertexIndex

  /**
   * Retrieves the adjacency matrix of the graph.
   *
   * The adjacency matrix is a 2D array where the rows and columns represent the vertices of the graph.
   * Each element represents the weight of the edge between the corresponding vertices.
   * If there is no edge between two vertices, a null value is used.
   *
   * @return a 2D array of nullable Doubles representing the adjacency matrix if the graph is non-empty,
   *         or null if the graph is empty
   */
  fun getAdjacencyMatrix(): Array<Array<Double?>>? = null

  /**
   * Retrieves the adjacency list representation of the graph.
   *
   * The adjacency list is an array where each element corresponds
   * to a vertex in the graph. Each element is a TreeMap that maps
   * the ID of an adjacent vertex to the weight of the edge connecting them.
   *
   * @return an array of TreeMap objects representing the adjacency list of the graph if it is non-empty,
   *         or null if the graph is empty.
   */
  fun getAdjacencyList(): Array<TreeMap<Int, Double>>? = null

}
