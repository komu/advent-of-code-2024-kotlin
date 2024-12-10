private class TrailMap(val rows: List<String>) {

    val trailheads = rows.withIndex().flatMap { (y, row) ->
        row.withIndex().filter { (_, c) -> c == '0' }.map { (x, _) -> Point(x, y) }
    }

    operator fun get(p: Point) = rows.getOrNull(p.y)?.getOrNull(p.x)
}

private fun pathEnds(map: TrailMap, start: Point): Collection<Point> {
    val nines = mutableListOf<Point>()

    fun recurse(p: Point, expectedLevel: Char) {
        if (map[p] == expectedLevel) {
            if (expectedLevel == '9')
                nines.add(p)
            else for (d in CardinalDirection.entries)
                recurse(p + d, expectedLevel + 1)
        }
    }

    recurse(start, '0')

    return nines
}

fun main() {
    fun part1(input: String): Int {
        val map = TrailMap(input.lines())
        return map.trailheads.sumOf { pathEnds(map, it).toSet().size }
    }

    fun part2(input: String): Int {
        val map = TrailMap(input.lines())
        return map.trailheads.sumOf { pathEnds(map, it).size }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
