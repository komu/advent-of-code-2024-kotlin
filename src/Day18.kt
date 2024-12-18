fun main() {

    fun pathLength(corrupted: Set<Point>, size: Int): Int? {
        val bounds = Bounds(0..size, 0..size)

        return shortestPathWithUniformCost(from = Point(0, 0), target = Point(size, size)) { p ->
            p.cardinalNeighbors.filter { it -> it in bounds && it !in corrupted }
        }?.cost
    }

    fun part1(input: String, size: Int = 70, take: Int = 1024): Int {
        val corrupted = input.lines().take(take).map(Point::parse).toSet()
        return pathLength(corrupted, size)!!
    }

    fun part2(input: String, size: Int = 70): String {
        val points = input.lines().map(Point::parse)

        val index = points.indices.binarySearchFirst { i ->
            pathLength(points.take(i + 1).toSet(), size) == null
        }

        return points[index!!].toString()
    }

    check(part1(readInput("Day18_test"), size = 6, take = 12) == 22)
    check(part2(readInput("Day18_test"), size = 6) == "6,1")

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
