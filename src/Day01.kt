import kotlin.math.abs

fun main() {
    val regex = Regex("""(\d+)\s+(\d+)""")

    fun parseRow(s: String): Pair<Int, Int> =
        regex.matchEntire(s)?.destructured?.let { (x, y) -> Pair(x.toInt(), y.toInt()) } ?: error("invalid '$s'")

    fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
        val data = input.map { parseRow(it) }
        return Pair(data.map { it.first }, data.map { it.second })
    }

    fun part1(input: List<String>): Int {
        val (xs, ys) = parse(input)
        return xs.sorted().zip(ys.sorted()).sumOf { (x, y) -> abs(x - y) }
    }

    fun part2(input: List<String>): Int {
        val (xs, ys) = parse(input)
        return xs.sumOf { x -> x * ys.count { y -> x == y } }
    }

    val testInput = readInput("Day01_test")

    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
