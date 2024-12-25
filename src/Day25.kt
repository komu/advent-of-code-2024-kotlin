fun main() {

    fun parse(input: String): Pair<List<List<Int>>, List<List<Int>>> {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()

        for (entry in input.split("\n\n")) {
            val heights = entry.lines().map { it.toList() }.transpose().map { it.countValues('#') }
            when {
                entry.startsWith(".....") -> keys.add(heights)
                entry.startsWith("#####") -> locks.add(heights)
                else -> error("invalid $entry")
            }
        }

        return Pair(locks, keys)
    }

    fun overlaps(key: List<Int>, lock: List<Int>) =
        key.zip(lock).all { (x,y) -> x + y < 8 }

    fun part1(input: String): Int {
        val (locks, keys) = parse(input)
        return locks.sumOf { lock -> keys.count { key -> overlaps(key, lock) }}
    }

    check(part1(readInput("Day25_test")) == 3)

    val input = readInput("Day25")
    part1(input).println()
}
