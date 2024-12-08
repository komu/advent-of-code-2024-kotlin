private fun antinodes(antennas: List<Point>, range: IntRange, bounds: Bounds) = buildSet {
    for ((a, b) in antennas.choosePairs()) {
        val v = a - b

        for (i in range) {
            val p = a + i * v
            if (p in bounds)
                add(p)
            else
                break
        }

        for (i in range) {
            val p = b - i * v
            if (p in bounds)
                add(p)
            else
                break
        }
    }
}

fun main() {
    fun solve(input: String, range: IntRange): Int {
        val rows = input.lines()
        val bounds = Bounds(xRange = rows[0].indices, yRange = rows.indices)

        return rows.withIndex()
            .flatMap { (y, row) -> row.withIndex().map { (x, v) -> v to Point(x, y) } }
            .filter { (c, _) -> c != '.' }
            .groupBy({ it.first }, { it.second })
            .flatMap { (_, ps) -> antinodes(ps, range, bounds) }.toSet().size
    }

fun part1(input: String) = solve(input, 1..1)
fun part2(input: String) = solve(input, 0..Int.MAX_VALUE)

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
