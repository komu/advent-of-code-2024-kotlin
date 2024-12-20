import kotlin.math.abs
import kotlin.time.measureTime

fun main() {

    fun solveCounts(input: String, distance: Int, threshold: Int): Map<Int, Int> {
        val track = CharGrid(input)
        val start = track.find('S')!!
        val end = track.find('E')!!
        val fromStart = track.distancesFrom(start) { it != '#' }
        val fromEnd = track.distancesFrom(end) { it != '#' }
        val normalCost = fromStart[end]

        val gainCounts = mutableMapOf<Int, Int>()

        for (startX in track.yRange) {
            for (startY in track.xRange) {
                if (track[startX, startY] == '#') continue

                for (dy in -distance..distance) {
                    val dxRange = distance - abs(dy)
                    for (dx in -dxRange..dxRange) {
                        val endX = startX + dx
                        val endY = startY + dy

                        if (track[endX, endY] == '#') continue

                        val cheatCost = abs(dx) + abs(dy)
                        if (cheatCost > 1 && track.contains(endX, endY)) {
                            val cost = cheatCost + fromStart[startX, startY] + fromEnd[endX, endY]
                            val gain = normalCost - cost
                            if (gain >= threshold)
                                gainCounts[gain] = gainCounts.getOrDefault(gain, 0) + 1
                        }
                    }
                }
            }
        }
        return gainCounts
    }

    fun countOverThreshold(input: String, distance: Int, threshold: Int) =
        solveCounts(input, distance, threshold).values.sum()

    fun part1(input: String) = countOverThreshold(input, distance = 2, threshold = 100)
    fun part2(input: String) = countOverThreshold(input, distance = 20, threshold = 100)

    // @formatter:off
    check(solveCounts(readInput("Day20_test"), 2, 1) == mapOf(2 to 14, 4 to 14, 6 to 2, 8 to 4, 10 to 2, 12 to 3, 20 to 1, 36 to 1, 38 to 1, 40 to 1, 64 to 1))
    // @formatter:on

    val input = readInput("Day20")
    measureTime {
        part1(input).println()
        part2(input).println()
    }.println()
}
