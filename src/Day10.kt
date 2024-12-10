class TrailMap(val rows: List<String>) {

    val coordinates = rows.first().indices.flatMap { x -> rows.indices.map { y -> Point(x, y) } }
    val trailheads = coordinates.filter { this[it] == 0 }
    operator fun get(p: Point): Int? = rows.getOrNull(p.y)?.getOrNull(p.x)?.digitToInt()

    fun pathEnds(start: Point): Collection<Point> {
        val nines = mutableListOf<Point>()

        fun recurse(p: Point, level: Int) {
            if (level == 9) {
                nines.add(p)
                return
            }
            val next = level + 1

            for (d in CardinalDirection.entries) {
                val neighbor = p + d
                if (this[neighbor] == next) {
                    recurse(neighbor, next)
                }
            }
        }

        recurse(start, 0)

        return nines
    }
}

fun main() {
    fun part1(input: String): Int {
        val map = TrailMap(input.lines())
        return map.trailheads.sumOf { map.pathEnds(it).toSet().size }
    }

    fun part2(input: String): Int {
        val map = TrailMap(input.lines())
        return map.trailheads.sumOf { map.pathEnds(it).size }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
