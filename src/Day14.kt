private class Robot(var p: Point, val v: Vec2)

private class RobotSpace(val w: Int, val h: Int, val robots: List<Robot>) {

    fun tick() {
        for (robot in robots)
            robot.p = Point(
                x = (robot.p.x + robot.v.x + w) % w,
                y = (robot.p.y + robot.v.y + h) % h,
            )
    }

    fun quadrantOf(p: Point): Int {
        val midX = w / 2
        val midY = h / 2

        return when {
            p.x < midX && p.y < midY -> 1
            p.x > midX && p.y < midY -> 2
            p.x < midX && p.y > midY -> 3
            p.x > midX && p.y > midY -> 4
            else -> 0
        }
    }

    fun hasTree(): Boolean {
        val points = PointSet(w, h)

        for (robot in robots)
            points.add(robot.p)

        return robots.any { points.containsSums(it.p, neighbors) }
    }

    fun checksum() =
        robots.groupingBy { quadrantOf(it.p) }
            .eachCount()
            .filter { it.key != 0 }
            .values
            .product()

    companion object {
        val neighbors = Direction.entries.map { it.toVec() }
    }
}

fun main() {

    fun parse(input: String, w: Int, h: Int): RobotSpace {
        val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")

        return RobotSpace(w, h, robots = input.lines().map { s ->
            val (x, y, vx, vy) = regex.matchEntire(s)?.destructured ?: error("invalid input line '$s'")
            Robot(Point(x.toInt(), y.toInt()), Vec2(vx.toInt(), vy.toInt()))
        })
    }

    fun part1(input: String, w: Int, h: Int): Int {
        val space = parse(input, w, h)

        repeat(100) {
            space.tick()
        }

        return space.checksum()
    }

    fun part2(input: String, w: Int, h: Int): Int {
        val space = parse(input, w, h)

        repeat(Int.MAX_VALUE) { seconds ->
            if (space.hasTree())
                return seconds

            space.tick()
        }

        error("tree not found")
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput, 11, 7) == 12)

    val input = readInput("Day14")
    part1(input, 101, 103).println()
    part2(input, 101, 103).println()
}
