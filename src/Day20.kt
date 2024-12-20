import kotlin.math.abs

fun main() {

    data class Cheat(val start: Point, val end: Point, val cost: Int)

    fun generateCheats(track: CharGrid, maxCost: Int): Set<Cheat> {
        val cheats = mutableSetOf<Cheat>()

        for (startX in track.yRange) {
            for (startY in track.xRange) {
                if (track[startX, startY] == '#') continue
                val start = Point(startX, startY)

                for (dy in -maxCost..maxCost) {
                    val xRange = maxCost - abs(dy)
                    for (dx in -xRange..xRange) {
                        val endX = startX + dx
                        val endY = startY + dy
                        val cost = abs(dx) + abs(dy)

                        if (cost in 2..maxCost && track.contains(endX, endY) && track[endX, endY] != '#') {
                            cheats += Cheat(start, Point(endX, endY), cost)
                        }
                    }
                }
            }
        }

        return cheats
    }

    fun solveCounts(input: String, distance: Int): Map<Int, Int> {
        val track = CharGrid(input)
        val start = track.find('S')!!
        val end = track.find('E')!!
        val fromStart = track.distancesFrom(start) { it != '#' }
        val fromEnd = track.distancesFrom(end) { it != '#' }

        val gainCounts = mutableMapOf<Int, Int>()
        for (cheat in generateCheats(track, distance)) {
            val cost = cheat.cost + fromStart[cheat.start] + fromEnd[cheat.end]
            val gain = fromStart[end] - cost
            if (gain > 0)
                gainCounts[gain] = gainCounts.getOrDefault(gain, 0) + 1
        }
        return gainCounts
    }

    fun countOverThreshold(input: String, distance: Int, threshold: Int) =
        solveCounts(input, distance).filterKeys { it >= threshold }.values.sum()

    fun part1(input: String) = countOverThreshold(input, distance = 2, threshold = 100)
    fun part2(input: String) = countOverThreshold(input, distance = 20, threshold = 100)

    // @formatter:off
    check(solveCounts(readInput("Day20_test"), 2) == mapOf(2 to 14, 4 to 14, 6 to 2, 8 to 4, 10 to 2, 12 to 3, 20 to 1, 36 to 1, 38 to 1, 40 to 1, 64 to 1))
    // @formatter:on

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
