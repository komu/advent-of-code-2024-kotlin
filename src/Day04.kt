import Direction.*

private class LetterGrid(val rows: List<String>) {
    val width = rows.first().length
    val coordinates = (0..<width).flatMap { x -> rows.indices.map { y -> Point(x, y) } }

    operator fun get(p: Point) = rows.getOrNull(p.y)?.getOrNull(p.x) ?: '.'
    operator fun get(x: Int, y: Int) = rows.getOrNull(y)?.getOrNull(x) ?: '.'
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = LetterGrid(input)

        return grid.coordinates.sumOf { p ->
            Direction.entries.count { d ->
                "XMAS".withIndex().all { (i, c) ->
                    grid[p + i * d] == c
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = LetterGrid(input)
        val validSignatures = setOf("AMMSS", "AMSSM", "ASMMS", "ASSMM")

        return grid.coordinates.count { p ->
            val signature = charArrayOf(grid[p], grid[p + NW], grid[p + NE], grid[p + SE], grid[p + SW]).concatToString()
            signature in validSignatures
        }
    }

    val testInput = readInputLines("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInputLines("Day04")
    part1(input).println()
    part2(input).println()
}
