fun main() {

    fun parse(input: String): Pair<List<String>, List<String>> {
        val (s1, s2) = input.split("\n\n")
        return Pair(s1.split(", "), s2.lines())
    }

    fun isPossible(design: String, patterns: List<String>): Boolean {
        val mutableMapOf = mutableMapOf<String, Boolean>()

        fun recurse(design: String): Boolean = mutableMapOf.getOrPut(design) {
            design.isEmpty() || patterns.any { design.startsWith(it) && recurse(design.removeRange(0, it.length)) }
        }

        return recurse(design)
    }

    fun waysToMake(design: String, patterns: List<String>): Long {
        val mutableMapOf = mutableMapOf<String, Long>()

        fun recurse(design: String): Long = mutableMapOf.getOrPut(design) {
            if (design.isEmpty())
                1
            else
                patterns.sumOf { if (design.startsWith(it)) recurse(design.removeRange(0, it.length)) else 0 }
        }

        return recurse(design)
    }

    fun part1(input: String): Int {
        val (patterns, designs) = parse(input)

        return designs.count { isPossible(it, patterns) }
    }

    fun part2(input: String): Long {
        val (patterns, designs) = parse(input)

        return designs.sumOf { waysToMake(it, patterns) }
    }

    check(part1(readInput("Day19_test")) == 6)
    check(part2(readInput("Day19_test")) == 16L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
