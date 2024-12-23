private fun <T> findMaximalCliques(g: Map<T, Set<T>>): List<Set<T>> {
    val cliques = mutableListOf<Set<T>>()

    fun recurse(r: Set<T>, p: Set<T>, x: Set<T>) {
        if (p.isEmpty() && x.isEmpty()) {
            cliques.add(r)
            return
        }

        val pivot = p.firstOrNull() ?: x.firstOrNull() ?: return
        val pivotNeighbors = g.neighbors(pivot)

        val newP = p.toMutableSet()
        val newX = x.toMutableSet()

        for (v in p) {
            if (v in pivotNeighbors) continue

            val ns = g.neighbors(v)
            recurse(r + v, newP.intersect(ns), newX.intersect(ns))
            newP.remove(v)
            newX.add(v)
        }
    }

    recurse(emptySet(), g.keys, emptySet())
    return cliques
}

fun main() {

    fun parseGraph(input: String) = buildMap {
        for (line in input.lines()) {
            val (x, y) = line.split('-')
            getOrPut(x) { mutableSetOf() }.add(y)
            getOrPut(y) { mutableSetOf() }.add(x)
        }
    }

    fun part1(input: String): Int {
        val g = parseGraph(input)
        val result = mutableSetOf<Set<String>>()

        for (a in g.keys.filter { it.startsWith('t') })
            for ((b, c) in g.neighbors(a).choosePairs())
                if (b in g.neighbors(c))
                    result.add(setOf(a, b, c))

        return result.size
    }

    fun part2(input: String) =
        findMaximalCliques(parseGraph(input)).maxBy { it.size }.sorted().joinToString(",")

    check(part1(readInput("Day23_test")) == 7)
    check(part2(readInput("Day23_test")) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
