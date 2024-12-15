import CardinalDirection.*

private class Warehouse(input: String) {
    val grid = MutableCharGrid(input)
    val boxes = grid.points.filter { grid[it] == '[' }.map { LargeBox(it) }.toMutableList()

    fun tryMove(point: Point, direction: CardinalDirection): Boolean {
        var p = point + direction
        var firstBox: Point? = null
        while (true) {
            val next = grid[p]!!
            when (next) {
                '.' -> {
                    if (firstBox != null) {
                        grid[p] = 'O'
                        grid[firstBox] = '.'
                    }

                    return true
                }

                '#' -> return false
                'O' -> {
                    if (firstBox == null) {
                        firstBox = p
                    }

                }

                else -> {
                    error("invalid '$next' at $p")
                }
            }

            p += direction
        }
        return false
    }

    fun canMoveInto(target: Point, d: CardinalDirection): Boolean {
        val value = grid[target]!!
        if (value == '#')
            return false

        val box = boxes.find { it.contains(target) }
        if (box == null)
            return true

        return when (d) {
            N, S -> canMoveInto(box.p1 + d, d) && canMoveInto(box.p2 + d, d)
            W -> canMoveInto(box.p1 + d, d)
            E -> canMoveInto(box.p2 + d, d)
        }
    }

    fun push(target: Point, d: CardinalDirection) {
        check(target in grid) { "out of bounds $target, ${grid.xRange} ${grid.yRange}" }
        val value = grid[target]!!
        if (value == '#')
            return

        val box = boxes.find { it.contains(target) }
        if (box == null)
            return

        when (d) {
            N, S -> {
                push(box.p1 + d, d)
                push(box.p2 + d, d)
            }
            W -> push(box.p1 + d, d)
            E -> push(box.p2 + d, d)
        }
        box.push(d)
    }

    fun gps() = grid.points.sumOf { p ->
        if (grid[p] == 'O')
            100 * p.y + p.x
        else
            0
    }
}

private class LargeBox(var p1: Point) {
    val p2: Point
        get() = p1 + E

    operator fun contains(p: Point) = p == p1 || p == p2

    fun push(d: CardinalDirection) {
        p1 += d
    }

    fun gps() = 100 * p1.y + p1.x
}

fun main() {

    fun parse(input: String, part2: Boolean = false): Pair<Warehouse, List<CardinalDirection>> {
        var (p1, p2) = input.split("\n\n")

        if (part2)
            p1 = p1.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.")

        val warehouse = Warehouse(p1)
        val moves = p2.removeNewlines().map { CardinalDirection.from(it) }

        return Pair(warehouse, moves)
    }

    fun part1(input: String): Int {
        val (warehouse, moves) = parse(input)

        var robot = warehouse.grid.find('@') ?: error("no robot")
        warehouse.grid[robot] = '.'

        for (move in moves) {
            if (warehouse.tryMove(robot, move)) {
                robot += move
            }
        }

        return warehouse.gps()
    }

    fun part2(input: String): Int {
        val (warehouse, moves) = parse(input, true)

        var robot = warehouse.grid.find('@') ?: error("no robot")
        warehouse.grid[robot] = '.'

        val boxes = warehouse.boxes
        for (p in warehouse.grid.points) {
            if (warehouse.grid[p] == '[' || warehouse.grid[p] == ']')
                warehouse.grid[p] = '.'
        }

        for (move in moves) {
            val target = robot + move
            if (warehouse.canMoveInto(target, move)) {
                warehouse.push(target, move)
                robot = target
            }
        }

        return boxes.sumOf { it.gps() }
    }

    check(part1(readInput("Day15_test")) == 2028)
    check(part1(readInput("Day15_test2")) == 10092)

    check(part2(readInput("Day15_test3")) == 618)
    check(part2(readInput("Day15_test2")) == 9021)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
