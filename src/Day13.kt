import kotlin.math.floor

fun main() {
    fun tokens(a: Vec2D, b: Vec2D, c: Vec2D): Long {
        // Cramer's rule
        val pa = det(c, b) / det(a, b)
        val pb = det(a, c) / det(a, b)

        return if (pa == floor(pa) && pb == floor(pb))
            (3 * pa + pb).toLong()
        else
            0
    }

    fun solve(input: String, offset: Long): Long {
        val regex = Regex(""".+: X[+=](\d+), Y[+=](\d+)""")

        val delta = Vec2D(offset.toDouble(), offset.toDouble())
        return input.split("\n\n").sumOf { s ->
            val (a, b, r) = s.lines().map { line ->
                val (x, y) = regex.matchEntire(line)?.destructured ?: error("invalid input line '$line'")
                Vec2D(x.toDouble(), y.toDouble())
            }

            tokens(a, b, r + delta)
        }
    }

    fun part1(input: String) = solve(input, offset = 0)
    fun part2(input: String) = solve(input, offset = 10000000000000)

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)
    check(part2(testInput) == 875318608908)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
