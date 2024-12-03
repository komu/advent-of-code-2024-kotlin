fun main() {
    fun part1(input: String) =
        Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(input).sumOf { match ->
            val (x, y) = match.destructured
            x.toInt() * y.toInt()
        }

    fun part2(input: String): Int {
        var enabled = true
        var sum = 0

        for (match in Regex("""(mul\((\d{1,3}),(\d{1,3})\))|(do(n't)?\(\))""").findAll(input)) {
            when (match.value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) {
                    val (_, x, y) = match.destructured
                    sum += x.toInt() * y.toInt()
                }
            }
        }

        return sum
    }

    check(part1(readInput("Day03_test1")) == 161)
    check(part2(readInput("Day03_test2")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
