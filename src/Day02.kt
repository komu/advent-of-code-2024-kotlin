fun main() {
    fun parse(s: String) = s.split(" ").map { it.toInt() }

    fun List<Int>.isSafe(): Boolean {
        val pairs = zipWithNext()
        return pairs.all { (a, b) -> a - b in 1..3 } || pairs.all { (a, b) -> b - a in 1..3 }
    }

    fun List<Int>.variationsWithSingleRemoved() =
        indices.map { i -> withIndex().filter { it.index != i }.map { it.value } }

    fun part1(input: List<String>) =
        input.map { parse(it) }.count { it.isSafe() }

    fun part2(input: List<String>) =
        input.map { parse(it) }.count { it.variationsWithSingleRemoved().any { it.isSafe() } }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
