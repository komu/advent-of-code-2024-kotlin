import kotlin.time.measureTime

private class Graph(input: String) {

    private val graph = buildMap<String, MutableSet<String>> {
        for (line in input.lines()) {
            val (x, y) = line.split('-')
            getOrPut(x) { mutableSetOf() }.add(y)
            getOrPut(y) { mutableSetOf() }.add(x)
        }
    }

    val keys = graph.keys

    fun neighbors(node: String) = graph[node].orEmpty()
}

// https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
private fun Graph.bronKerbosch(
    R: Set<String>,
    P: Set<String>,
    X: Set<String>,
    cliques: MutableList<Set<String>>
) {
    if (P.isEmpty() && X.isEmpty()) {
        cliques.add(R) // R is a maximal clique
        return
    }

    val P = P.toMutableSet()
    val X = X.toMutableSet()

    // Optimization: choose a pivot to reduce recursive calls
    val pivot = (P + X).firstOrNull() ?: return
    val pivotNeighbors = neighbors(pivot)

    for (v in P - pivotNeighbors) {
        val neighbors = neighbors(v)
        bronKerbosch(
            R + v,
            P.intersect(neighbors),
            X.intersect(neighbors),
            cliques
        )
        P.remove(v)
        X.add(v)
    }
}

private fun findMaximalCliques(g: Graph): List<Set<String>> {
    val cliques = mutableListOf<Set<String>>()
    g.bronKerbosch(
        R = emptySet(),
        P = g.keys,
        X = emptySet(),
        cliques = cliques
    )
    return cliques
}

fun main() {

    fun part1(input: String): Int {
        val g = Graph(input)
        val result = mutableSetOf<String>()

        for (a in g.keys.filter { it.startsWith('t') })
            for (b in g.neighbors(a))
                for (c in g.neighbors(a))
                    if (b != c && b in g.neighbors(c))
                        result.add(listOf(a, b, c).sorted().joinToString(","))

        return result.size
    }

    fun part2(input: String) =
        findMaximalCliques(Graph(input)).maxBy { it.size }.sorted().joinToString(",").trace("new")

    check(part1(readInput("Day23_test")) == 7)
    check(part2(readInput("Day23_test")) == "co,de,ka,ta")

    val input = readInput("Day23")
    measureTime {
        part1(input).println()
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}
