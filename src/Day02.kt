fun main() {
    fun parse(s: String) = s.split(" ").map { it.toInt() }

    fun List<Int>.isSafe(range: IntRange, tolerate: Boolean = false): Boolean {
        var mayIgnore = tolerate

        var prev = first()
        for ((i, value) in withIndex())
            when {
                i == 0 -> {}
                value - prev in range -> prev = value
                mayIgnore -> mayIgnore = false
                else -> return false
            }

        return true
    }

    fun List<Int>.isSafe(tolerate: Boolean) =
        isSafe(1..3, tolerate) || isSafe(-3..-1, tolerate)

    fun part1(input: List<String>) =
        input.count { parse(it).isSafe(tolerate = false) }

    fun part2(input: List<String>) =
        input.count { parse(it).isSafe(tolerate = true) }

    val testInput = readInputLines("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInputLines("Day02")
    part1(input).println()
    part2(input).println()
}
