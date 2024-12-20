/** Converts character grid to int-grid so that each region gets its own numeric non-zero id. */
private fun CharGrid.normalizeRegions(): IntGrid {
    val uninitialized = 0
    val result = IntGrid(width, height, uninitialized)

    var id = 1
    for ((p, type) in pointsWithValues)
        if (result[p] == uninitialized)
            result.flood(p, value = id++, Point::cardinalNeighbors) { this[it] == type && result[it] == uninitialized }

    return result
}

private fun extractRegions(input: String): Collection<Set<Point>> {
    val grid = CharGrid(input).normalizeRegions()
    return grid.pointsWithValues
        .groupBy({ it.second }, { it.first })
        .values
        .map { it.toSet() }
}

private fun perimeter(region: Set<Point>): Int {
    val adjacent = region.choosePairs().count { (a, b) -> a.isCardinalNeighbor(b) }
    return 4 * region.size - 2 * adjacent
}

private fun sides(region: Set<Point>): Int {
    // Divide the area into 2x2 blocks
    val offsets = listOf(Vec2(0, 0), Vec2(0, 1), Vec2(1, 0), Vec2(1, 1))
    val blocks = region.flatMap { it - offsets }.toSet().map { it + offsets }

    var corners = 0

    for (block in blocks) {
        val ps = block.filter { it in region }
        when (ps.size) {
            1, 3 -> corners++
            2 -> if (ps[0].isDiagonalNeighbor(ps[1]))
                corners += 2
        }
    }

    return corners // number of sides is equal to number of corners
}

fun main() {
    fun part1(input: String): Int =
        extractRegions(input).sumOf { it.size * perimeter(it) }

    fun part2(input: String): Int =
        extractRegions(input).sumOf { it.size * sides(it) }

    check(part1(readInput("Day12_test1")) == 140)
    check(part1(readInput("Day12_test2")) == 772)
    check(part1(readInput("Day12_test3")) == 1930)
    check(part2(readInput("Day12_test1")) == 80)
    check(part2(readInput("Day12_test2")) == 436)
    check(part2(readInput("Day12_test3")) == 1206)
    check(part2(readInput("Day12_test4")) == 236)
    check(part2(readInput("Day12_test5")) == 368)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
