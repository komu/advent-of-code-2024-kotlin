fun main() {

    fun splitStone(stone: Long): Pair<Long, Long> {
        val div = pow10(countDigits(stone) / 2)
        return Pair(stone / div, stone % div)
    }

    fun solve(input: String, totalRounds: Int): Long {
        val stones = input.split(" ").map { it.toLong() }
        val cache = mutableMapOf<Pair<Long, Int>, Long>()

        fun recurse(stone: Long, rounds: Int): Long = cache.getOrPut(stone to rounds) {
            when {
                rounds == 0 -> 1
                stone == 0L -> recurse(1L, rounds - 1)
                countDigits(stone) % 2 == 0 -> {
                    val (l, r) = splitStone(stone)
                    recurse(l, rounds - 1) + recurse(r, rounds - 1)
                }
                else -> recurse(stone * 2024, rounds - 1)
            }
        }

        return stones.sumOf { recurse(it, totalRounds) }
    }

    fun part1(input: String) = solve(input, 25)
    fun part2(input: String) = solve(input, 75)

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)
    check(part2(testInput) == 65601038650482L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
