
fun main() {

    fun pathEnds(map: CharGrid, start: Point): Collection<Point> {
        val nines = mutableListOf<Point>()

        fun recurse(p: Point, expectedLevel: Char) {
            if (map[p] == expectedLevel) {
                if (expectedLevel == '9')
                    nines.add(p)
                else for (n in p.cardinalNeighbors)
                    recurse(n, expectedLevel + 1)
            }
        }

        recurse(start, '0')

        return nines
    }

    fun solve(input: String): Collection<Collection<Point>> {
        val map = CharGrid(input)
        return map.findAll('0').map { pathEnds(map, it) }
    }

    fun part1(input: String) = solve(input).sumOf { it.toSet().size }
    fun part2(input: String) = solve(input).sumOf { it.size }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
