
fun main() {

    fun ruleComparator(rules: List<Pair<Int, Int>>) = object : Comparator<Int> {
        val beforeThan = rules.groupBy({ it.first }, { it.second })

        override fun compare(o1: Int, o2: Int): Int = when {
            o1 in beforeThan[o2].orEmpty() -> 1
            o2 in beforeThan[o1].orEmpty() -> -1
            else -> 0
        }
    }

    fun parse(s: String): Pair<Comparator<Int>, List<List<Int>>> {
        val regex = Regex("""(\d+)\|(\d+)""")
        val (rulesStr, updatesStr) = s.split("\n\n")

        val rules = rulesStr.lines().map {
            regex.matchEntire(it)?.destructured?.let { (x, y) -> Pair(x.toInt(), y.toInt()) }
                ?: error("invalid '$rulesStr'")
        }

        val updates = updatesStr.lines().map { it.split(",").map { it.toInt() } }

        return Pair(ruleComparator(rules), updates)
    }

    fun part1(input: String): Int {
        val (ruleComparator, updates) = parse(input)
        return updates
            .filter { it.sortedWith(ruleComparator) == it }
            .sumOf { it[it.size / 2] }
    }

    fun part2(input: String): Int {
        val (ruleComparator, updates) = parse(input)
        return updates
            .filterNot { it.sortedWith(ruleComparator) == it }
            .map { it.sortedWith(ruleComparator) }
            .sumOf { it[it.size / 2] }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    check(part1(input) == 5732)
    check(part2(input) == 4716)
    part1(input).println()
    part2(input).println()
}

