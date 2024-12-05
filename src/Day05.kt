fun main() {

    fun ruleComparator(beforeThan: Map<Int, Collection<Int>>) = object : Comparator<Int> {
        override fun compare(o1: Int, o2: Int) = when {
            o1 in beforeThan[o2].orEmpty() -> 1
            o2 in beforeThan[o1].orEmpty() -> -1
            else -> 0
        }
    }

    fun parse(s: String): Pair<Comparator<Int>, List<List<Int>>> {
        val (rulesStr, updatesStr) = s.split("\n\n", limit = 2)

        val beforeThan = rulesStr.lines()
            .map { it.split('|', limit = 2).map { it.toInt() } }
            .groupBy({ it[0] }, { it[1] })

        val updates = updatesStr.lines()
            .map { it.split(",").map { it.toInt() } }

        return Pair(ruleComparator(beforeThan), updates)
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
    part1(input).println()
    part2(input).println()
}

