private class AntennaMap(val rows: List<String>) {

    val coordinates = rows.first().indices.flatMap { x -> rows.indices.map { y -> Point(x, y) } }.toSet()
    operator fun get(p: Point): Char? = rows.getOrNull(p.y)?.getOrNull(p.x)
    operator fun contains(p: Point) = p in coordinates

    val antennasByType = coordinates
        .map { it to this[it]!! }
        .filter { it.second != '.' }
        .groupBy({ it.second }, { it.first })
}

fun main() {

    fun part1(input: String): Int {
        val map = AntennaMap(input.lines())

        fun antinodes(points: List<Point>) = buildSet {
            for ((a, b) in points.choosePairs()) {
                val v = a - b

                val p1 = a + v
                if (p1 in map)
                    add(p1)

                val p2 = b - v
                if (p2 in map)
                    add(p2)
            }
        }

        return map.antennasByType.values.flatMap { antinodes(it) }.toSet().size
    }

    fun part2(input: String): Int {
        val map = AntennaMap(input.lines())

        fun antinodes2(points: List<Point>) = buildSet {
            for ((a, b) in points.choosePairs()) {
                val v = a - b

                var p1 = a
                while (p1 in map) {
                    add(p1)
                    p1 += v
                }

                var p2 = b
                while (p2 in map) {
                    add(p2)
                    p2 -= v
                }
            }
        }

        return map.antennasByType.values.flatMap { antinodes2(it) }.toSet().size
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
