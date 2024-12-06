import kotlin.time.measureTime

private data class LabMap(val rows: List<String>) {

    val coordinates = rows.first().indices.flatMap { x -> rows.indices.map { y -> Point(x, y) } }
    val start = coordinates.find { this[it] == '^' } ?: error("no start")

    operator fun get(p: Point): Char? =
        rows.getOrNull(p.y)?.getOrNull(p.x)

    fun path(): Collection<Point> {
        val seen = mutableSetOf<Point>()

        var d = CardinalDirection.N
        var p = start

        while (this[p] != null) {
            seen += p

            while (this[p + d] == '#')
                d = d.clockwise()

            p += d
        }

        return seen
    }

    fun hasLoop(obstruction: Point): Boolean {
        var d = CardinalDirection.N
        var p = start
        val seen = mutableSetOf<Pair<Point, CardinalDirection>>()

        while (this[p] != null) {
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
}

fun main() {

    fun part1(input: List<String>): Int =
        LabMap(input).path().size

    fun part2(input: List<String>): Int {
        val map = LabMap(input)
        return map.path().count { it != map.start && map.hasLoop(it) }
    }

    val testInput = readInputLines("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInputLines("Day06")
    part1(input).println()
    part2(input).println()
}
