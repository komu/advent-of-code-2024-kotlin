fun main() {

    fun parse(input: String) =
        input.lines().map(Point::parse)

    fun findPath(corrupted: Set<Point>, size: Int): Int? {
        val start = Point(0, 0)
        val end = Point(size, size)
        val bounds = Bounds(0..size, 0..size)

        return shortestPathWithUniformCost(start, end) { p ->
            p.cardinalNeighbors.filter { it -> it in bounds && it !in corrupted }
        }?.cost
    }

    fun part1(input: String, size: Int = 70, take: Int = 1024): Int {
        val corrupted = parse(input).take(take).toSet()
        return findPath(corrupted, size) ?: error("no path found")
    }

    fun part2(input: String, size: Int = 70): String {
        val ps = parse(input)

        val index = ps.indices.binarySearchFirst { i ->
            findPath(ps.take(i + 1).toSet(), size) == null
        }!!

        return ps[index].toString()
    }

    check(part1(readInput("Day18_test"), size = 6, take = 12) == 22)
    check(part2(readInput("Day18_test"), size = 6) == "6,1")

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
