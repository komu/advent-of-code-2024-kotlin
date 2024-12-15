import CardinalDirection.*

private class Box(var west: Point, val large: Boolean) {
    val east: Point
        get() = if (large) west + E else west

    val points: Set<Point>
        get() = setOf(west, east)

    operator fun contains(p: Point) = p == east || p == west

    fun push(d: CardinalDirection) {
        west += d
    }

    fun gps() = 100 * west.y + west.x
}

private class Warehouse(val boxes: Collection<Box>, val walls: Set<Point>, val robot: Point) {

    fun canMoveTowards(p: Point, d: CardinalDirection): Boolean {
        val target = p + d

        if (target in walls)
            return false

        val box = boxes.find { it.contains(target) }
        if (box == null)
            return true

        return when (d) {
            N, S -> box.points.all { canMoveTowards(it, d) }
            W -> canMoveTowards(box.west, d)
            E -> canMoveTowards(box.east, d)
        }
    }

    fun pushTowards(p: Point, d: CardinalDirection) {
        val target = p + d
        val box = boxes.find { it.contains(target) } ?: return

        when (d) {
            N, S -> box.points.forEach { pushTowards(it, d) }
            W -> pushTowards(box.west, d)
            E -> pushTowards(box.east, d)
        }
        box.push(d)
    }

    companion object {
        fun parse(input: String): Warehouse {
            val grid = CharGrid(input)

            return Warehouse(
                boxes = buildList {
                    for ((p, v) in grid.pointsWithValues)
                        when (v) {
                            '[' -> add(Box(p, large = true))
                            'O' -> add(Box(p, large = false))
                        }
                },
                walls = grid.findAll('#'),
                robot = grid.find('@') ?: error("no robot")
            )
        }
    }
}


fun main() {

    fun solve(input: String): Int {
        val (p1, p2) = input.split("\n\n")

        val warehouse = Warehouse.parse(p1)
        val moves = p2.removeNewlines().map { CardinalDirection.from(it) }
        var robot = warehouse.robot

        for (move in moves)
            if (warehouse.canMoveTowards(robot, move)) {
                warehouse.pushTowards(robot, move)
                robot += move
            }

        return warehouse.boxes.sumOf { it.gps() }
    }

    fun String.widen() = replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.")

    fun part1(input: String) = solve(input)
    fun part2(input: String) = solve(input.widen())

    check(part1(readInput("Day15_test")) == 2028)
    check(part1(readInput("Day15_test2")) == 10092)
    check(part2(readInput("Day15_test3")) == 618)
    check(part2(readInput("Day15_test2")) == 9021)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
