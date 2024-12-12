private fun extractRegions(input: String): List<Set<Point>> {
    val rows = input.lines()

    fun get(p: Point) = rows.getOrNull(p.y)?.getOrNull(p.x) ?: '.'

    val seen = mutableSetOf<Point>()

    return buildList {
        for ((p, type) in valuesWithPoints(rows)) {
            if (p in seen) continue

            val points = floodFillCardinal(p) { get(it) == type }
            add(points)
            seen += points
        }
    }
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

    check(part1(readInput("Day12_test1")).trace("part 1a") == 140)
    check(part1(readInput("Day12_test2")).trace("part 2b") == 772)
    check(part1(readInput("Day12_test3")).trace("part 3c") == 1930)
    check(part2(readInput("Day12_test1")).trace("part 2a") == 80)
    check(part2(readInput("Day12_test2")).trace("part 2b") == 436)
    check(part2(readInput("Day12_test3")).trace("part 2c") == 1206)
    check(part2(readInput("Day12_test4")).trace("part 2d") == 236)
    check(part2(readInput("Day12_test5")).trace("part 2e") == 368)

    println("--")
    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()// 887536 is too low
}
