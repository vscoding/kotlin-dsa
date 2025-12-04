package io.intellij.dsa.graph.compute

import io.intellij.dsa.getLogger
import io.intellij.dsa.graph.Graph
import io.intellij.dsa.graph.GraphChecker
import io.intellij.dsa.graph.Vertex

/**
 * 环分析器
 *
 * @author tech@intellij.io
 * @since 2025-06-01
 */
class CycleAnalyzer(graph: Graph) : GraphChecker(graph) {

    companion object {
        val log = getLogger(CycleAnalyzer::class.java)
    }

    init {
        checkEmpty()
    }

    /**
     * 查找图中所有的环
     */
    fun findCycles(quickReturn: Boolean = false): Result {
        val record = Result(graph.isDirected())
        val vertices = graph.getVertexes()
        val visited = mutableSetOf<String>()

        // 全遍历
        for (vertex in vertices) {
            if (vertex.name !in visited) {
                dfs(vertex, visited, mutableListOf(), mutableSetOf(), record, 1, quickReturn)
            }
        }
        return record
    }

    /**
     * 深度优先搜索
     * 保证每个节点都被深度遍历一次
     * 某个节点在遍历的时候
     * 1. 如果邻居未被访问，则递归调用 DFS 继续搜索（注意传递了路径的副本)
     * 2. 如果邻居已被访问且在当前递归栈中，说明找到了一个环。记录从邻居到当前顶点的路径作为环
     * 3. 递归结束后，当前节点出栈
     */
    private fun dfs(
        from: Vertex,
        visited: MutableSet<String>,
        path: MutableList<Vertex>,
        marked: MutableSet<String>,
        record: Result,
        depth: Int,
        quickReturn: Boolean
    ): Boolean {
        visited.add(from.name)

        marked.add(from.name)
        path.add(from)

        val indent = "$depth  ${"- ".repeat(depth)}"
        log.debug(
            "{}开始遍历 {} 节点|visited={}|marked={}",
            indent,
            from.name,
            visited.joinToString(" "),
            marked.joinToString(" ")
        )

        val edges = graph.adjacentEdges(from.id)
        //  1. 如果邻居未被访问，则递归调用 DFS 继续搜索（注意传递了路径的副本)
        //  2. 如果邻居已被访问且在当前递归栈中，说明找到了一个环。记录从邻居到当前顶点的路径作为环
        edges.forEach { edge ->
            val to = edge.to
            log.debug("{}开始处理边 {} --> {}", indent, edge.from.name, to.name)

            if (to.name !in visited) {
                log.debug("{}没有访问过 {} 节点,深度遍历", indent, to.name)
                val hasCycle =
                    dfs(to, visited, path.toMutableList(), marked, record, depth + 1, quickReturn)
                if (hasCycle && quickReturn) {
                    return true
                }
            } else {
                if (to.name in marked) {
                    log.debug("{}在节点 {} 发现环|处理的边为 {}->{}", indent, from.name, edge.from.name, to.name)
                    if (quickReturn) {
                        return true
                    }
                    // 说明有环
                    val cycle = mutableListOf<Vertex>()
                    val start = path.indexOf(to)
                    for (i in start until path.size) {
                        cycle.add(path[i])
                    }
                    record.addCycle(cycle)
                } else {
                    // important
                    log.debug("{}重新处理 {} 节点; 边为 {}->{}", indent, to.name, edge.from.name, to.name)
                    dfs(to, visited, path.toMutableList(), marked, record, depth + 1, quickReturn)
                }
            }
        }

        // 出栈
        log.debug(
            "{}出栈 {} 节点，因为不确定是否还有其他点 ??? --> {} |visited={}|marked={}",
            indent, from.name, from.name, visited.joinToString(" "), marked.joinToString(" ")
        )
        marked.remove(from.name)
        log.debug(
            "{}完成处理 {} 节点|visited={}|marked={}",
            indent, from.name, visited.joinToString(" "), marked.joinToString(" ")
        )
        return record.cycles.isNotEmpty()
    }

    /**
     * 环检测结果
     */
    class Result internal constructor(private val directed: Boolean) {
        private val _cycles = mutableListOf<List<Vertex>>()
        val cycles: List<List<Vertex>> get() = _cycles.toList()

        private val cycleZip = mutableSetOf<String>()

        internal fun hasCycle() = cycles.isNotEmpty()

        internal fun addCycle(cycle: List<Vertex>) {
            if (!directed) {
                val zip = cycle.map { it.name }
                    .sorted()
                    .joinToString(" ")
                if (zip in cycleZip) {
                    return
                }
                cycleZip.add(zip)
                _cycles.add(cycle.toList())
            } else {
                _cycles.add(cycle.toList())
            }
        }

        /**
         * 打印所有找到的环
         */
        fun printCycles() {
            println("Cycles Found|Cycle's Number = ${cycles.size}")
            cycles.forEach(::printCycle)
        }

        /**
         * 打印单个环
         */
        private fun printCycle(cycle: List<Vertex>) {
            println("Printing Cycle|Vertex's Number = ${cycle.size}|Vertexes = ${cycle.joinToString(" ") { it.name }}")
            val lineStart = "  "

            // 打印环，如果只有两个节点，则打印成 A - B
            // 如果超过两个节点，打印成三行，打印出一个可以理解的环
            if (cycle.size == 2) {
                println("$lineStart${cycle[0].name} <=> ${cycle[1].name}")
                println()
                return
            }

            /*
              A -> B -> C
              |        |
              D - E - F
             */

            // 是否是偶数
            val isEven = (cycle.size % 2 == 0)
            val mid = if (isEven) cycle.size / 2 else cycle.size / 2 + 1

            // 打印上半部分
            val upper = cycle.subList(0, mid)
            // A -> B -> C
            val upBuilder = StringBuilder(lineStart)
            for (i in upper.indices) {
                if (i == upper.size - 1) {
                    upBuilder.append(upper[i].name)
                } else {
                    upBuilder.append(upper[i].name).append(
                        if (directed) " -> " else " - "
                    )
                }
            }
            println(upBuilder)

            // 打印中间部分
            val midStart = lineStart + if (directed) "↑" else "|"
            val midEnd = when {
                isEven -> if (directed) "↓" else "|"
                else -> if (directed) "↙" else "/"
            }
            val sub = when {
                isEven -> 2
                directed -> 4
                else -> 3
            }
            println(midStart + " ".repeat(upBuilder.length - lineStart.length - sub) + midEnd)

            // 打印下半部分
            // D - E - F
            val lower = cycle.subList(mid, cycle.size)
            val downBuilder = StringBuilder(lineStart)
            for (i in lower.indices.reversed()) {
                if (i == 0) {
                    downBuilder.append(lower[i].name)
                } else {
                    downBuilder.append(lower[i].name).append(
                        if (directed) " <- " else " - "
                    )
                }
            }
            println(downBuilder)
            println()
        }
    }
}