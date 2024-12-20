import java.util.stream.Collectors.summingInt
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {

    fun solveCounts(input: String, distance: Int, threshold: Int): Int {
        val track = CharGrid(input)
        val start = track.find('S')!!
        val end = track.find('E')!!
        val fromStart = track.distancesFrom(start) { it != '#' }
        val fromEnd = track.distancesFrom(end) { it != '#' }
        val normalCost = fromStart[end]

        return track.yRange.toList().parallelStream().collect(summingInt { startY ->
            track.xRange.sumOf { startX ->
                if (fromStart[startX, startY] == Int.MAX_VALUE)
                    0
                else {
                    (-distance..distance).sumOf { dy ->
                        val dxRange = distance - abs(dy)
                        (-dxRange..dxRange).count { dx ->
                            val endX = startX + dx
                            val endY = startY + dy
                            val cheatCost = abs(dx) + abs(dy)

                            if (cheatCost > 1 && track.contains(endX, endY) && fromEnd[endX, endY] != Int.MAX_VALUE) {
                                val cost = cheatCost + fromStart[startX, startY] + fromEnd[endX, endY]
                                val gain = normalCost - cost

                                gain >= threshold
                            } else {
                                false
                            }
                        }
                    }
                }
            }
        })
    }

    fun part1(input: String) = solveCounts(input, distance = 2, threshold = 100)
    fun part2(input: String) = solveCounts(input, distance = 20, threshold = 100)

    val testInput = readInput("Day20_test")
    check(solveCounts(testInput, 2, 64) == 1)
    check(solveCounts(testInput, 2, 40) == 2)
    check(solveCounts(testInput, 2, 38) == 3)
    check(solveCounts(testInput, 2, 36) == 4)
    check(solveCounts(testInput, 2, 4) == 30)

    val input = readInput("Day20")
    measureTime {
        part1(input).println()
        part2(input).println()
    }.println()
}
