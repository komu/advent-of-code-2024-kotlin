import java.util.stream.Collectors.summingLong

private class Equation(val total: Long, val xs: List<Long>) {

    fun isSatisfiable(vararg ops: (Long, Long) -> Long): Boolean {
        fun recurse(acc: Long, i: Int): Boolean = when {
            i == xs.size -> acc == total
            acc > total -> false
            else -> ops.any { op -> recurse(op(acc, xs[i]), i + 1) }
        }

        return recurse(xs.first(), 1)
    }
}

fun main() {

    fun parse(s: String): Equation {
        val (total, xs) = s.split(": ")
        return Equation(total.toLong(), xs.split(" ").map { it.toLong() })
    }

    fun solve(input: String, vararg ops: (Long, Long) -> Long) =
        input.lines()
            .parallelStream()
            .map { parse(it) }
            .filter { it.isSatisfiable(*ops) }
            .collect(summingLong { it.total })

    fun part1(input: String) = solve(input, Long::times, Long::plus)
    fun part2(input: String) = solve(input, Long::concat, Long::times, Long::plus)

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
