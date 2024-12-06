private data class LabMap(val rows: List<String>, val obstruction: Point? = null) {

    val coordinates = rows.first().indices.flatMap { x -> rows.indices.map { y -> Point(x, y) } }
    val start = coordinates.find { this[it] == '^' } ?: error("no start")

    operator fun get(p: Point) =
        if (p == obstruction) '#' else rows.getOrNull(p.y)?.getOrNull(p.x) ?: '_'

    fun loops(): Boolean {
        var d = CardinalDirection.N
        var p = start
        val seen = mutableSetOf<Pair<Point, CardinalDirection>>()

        while (this[p] != '_') {
            if (!seen.add(p to d))
                return true

            while (this[p + d] == '#')
                d = d.clockwise()

            p += d
        }

        return false
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val map = LabMap(input)
        val seen = mutableSetOf<Point>()

        var d = CardinalDirection.N
        var p = map.start

        while (map[p] != '_') {
            seen += p

            while (map[p + d] == '#')
                d = d.clockwise()

            p += d
        }

        return seen.size
    }

    fun part2(input: List<String>): Int {
        val map = LabMap(input)

        return map.coordinates.count { it != map.start && map.copy(obstruction = it).loops() }
    }

    val testInput = readInputLines("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInputLines("Day06")
    part1(input).println()
    part2(input).println()
}
