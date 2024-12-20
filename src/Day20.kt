
fun main() {

    data class Cheat(val start: Point, val end: Point, val cost: Int)

    fun CharGrid.pathLength(start: Point, end: Point): Int =
        shortestPathWithUniformCost(start, end) { it.cardinalNeighbors.filter { this[it] != '#' } }!!.cost

    fun generateCheats(endpoints: Collection<Point>, maxCost: Int): MutableSet<Cheat> {
        val cheats = mutableSetOf<Cheat>()

        for (start in endpoints) {
            for (end in endpoints) {
                val cost = start.manhattanDistance(end)
                if (cost > 1 && cost <= maxCost)
                    cheats += Cheat(start, end, cost)
            }
        }

        return cheats
    }

    fun solveCounts(input: String, distance: Int): Map<Int, Int> {
        val track = CharGrid(input)
        val start = track.find('S')!!
        val end = track.find('E')!!
        val validPoints = track.findAllNot('#')

        val cheats = generateCheats(validPoints, distance)

        val originalCost = track.pathLength(start, end)
        val pathsFromStart = validPoints.associateWith { track.pathLength(start, it) }
        val pathsToEnd = validPoints.associateWith { track.pathLength(it, end) }

        val gainCounts = mutableMapOf<Int, Int>()
        for (cheat in cheats.progressIterator()) {
            val cost = cheat.cost + pathsFromStart[cheat.start]!! + pathsToEnd[cheat.end]!!
            val gain = originalCost - cost
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
