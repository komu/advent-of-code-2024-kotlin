fun main() {
    fun parse(s: String) = s.split(" ").map { it.toInt() }

    fun List<Int>.isSafe(range: IntRange): Boolean {

        var prev = first()
        for (value in drop(1))
            if (value - prev in range)
                prev = value
            else
                return false

        return true
    }

    fun List<Int>.isSafe() =
        isSafe(1..3) || isSafe(-3..-1)

    fun List<Int>.variationsWithSingleRemoved() =
        indices.map { i -> withIndex().filter { it.index != i }.map { it.value } }

    fun part1(input: List<String>) =
        input.map { parse(it) }.count { it.isSafe() }

    fun part2(input: List<String>) =
        input.map { parse(it) }.count { it.variationsWithSingleRemoved().any { it.isSafe() } }

    val testInput = readInputLines("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInputLines("Day02")
    part1(input).println()
    part2(input).println()

    assertEquals(part1(input), 332)
    assertEquals(part2(input), 398)
}
