private class Graph(input: String) {

    private val neighborsByNode = buildMap<String, MutableSet<String>> {
        for (line in input.lines()) {
            val (x, y) = line.split('-')
            getOrPut(x) { mutableSetOf() }.add(y)
            getOrPut(y) { mutableSetOf() }.add(x)
        }
    }

    fun neighbors(node: String) = neighborsByNode[node].orEmpty()

    fun findCliques(): Collection<Set<String>> {
        val result = mutableListOf<Set<String>>()

        tailrec fun recurse(smallerCliques: Set<Set<String>>) {
            if (smallerCliques.isEmpty()) return

            result.addAll(smallerCliques)

            val newCliques = mutableSetOf<Set<String>>()
            for (clique in smallerCliques) {
                val node = clique.first()
                for (candidate in neighbors(node)) {
                    if (clique.all { candidate in neighbors(it) }) {
                        newCliques.add(clique + candidate)
                    }
                }
            }

            recurse(newCliques)
        }

        recurse(neighborsByNode.keys.map { setOf(it) }.toSet())

        return result
    }
}

fun main() {

    fun part1(input: String) = Graph(input).findCliques().count { it.size == 3 && it.any { it.startsWith("t") } }
    fun part2(input: String) = Graph(input).findCliques().maxBy { it.size }.sorted().joinToString(",")

    check(part1(readInput("Day23_test")) == 7)
    check(part2(readInput("Day23_test")) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
