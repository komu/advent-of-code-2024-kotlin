import Direction.*

fun main() {
    fun part1(input: String): Int {
        val grid = CharGrid(input)

        return grid.points.sumOf { p ->
            Direction.entries.count { d ->
                "XMAS".withIndex().all { (i, c) ->
                    grid[p + i * d] == c
                }
            }
        }
    }

    fun part2(input: String): Int {
        val grid = CharGrid(input)
        val deltas = listOf(Vec2.ZERO, NW.toVec(), NE.toVec(), SE.toVec(), SW.toVec())
        val validSignatures = setOf("AMMSS", "AMSSM", "ASMMS", "ASSMM")

        return grid.points.count { p ->
            val signature = deltas.map { grid[p + it] }.joinToString("")
            signature in validSignatures
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
