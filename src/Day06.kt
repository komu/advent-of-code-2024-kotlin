fun main() {

    fun CharGrid.hasLoop(obstruction: Point, start: Point): Boolean {
        var d = CardinalDirection.N
        var p = start
        val seen = mutableSetOf<Pair<Point, CardinalDirection>>()

        while (p in this) {
            var next = p + d

            while (this[next] == '#' || next == obstruction) {
                if (!seen.add(p to d))
                    return true

                d = d.clockwise()
                next = p + d
            }

            p = next
        }

        return false
    }

    fun CharGrid.path(start: Point): Collection<Point> {
        val seen = mutableSetOf<Point>()

        var d = CardinalDirection.N
        var p = start

        while (p in this) {
            seen += p

            while (this[p + d] == '#')
                d = d.clockwise()

            p += d
        }

        return seen
    }

    fun part1(input: String): Int {
        val map = CharGrid(input)
        val start = map.find('^') ?: error("no start")

        return map.path(start).size
    }

    fun part2(input: String): Int {
        val map = CharGrid(input)
        val start = map.find('^') ?: error("no start")

        val candidatesObstacles = map.path(start) - start
        return candidatesObstacles.count { map.hasLoop(it, start) }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
