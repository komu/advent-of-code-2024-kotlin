private class Robot(var p: Point, val v: Vec2) {
    fun move(space: Space = Space.real) {
        p = Point(
            x = (p.x + v.x + space.w) % space.w,
            y = (p.y + v.y + space.h) % space.h,
        )
    }
}

private class Space(val w: Int, val h: Int) {

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

    companion object {
        val test = Space(11, 7)
        val real = Space(101, 103)
    }
}

fun main() {

    fun parse(input: String): List<Robot> {
        val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")

        return input.lines().map { s ->
            val (x, y, vx, vy) = regex.matchEntire(s)?.destructured ?: error("invalid input line '$s'")
            Robot(Point(x.toInt(), y.toInt()), Vec2(vx.toInt(), vy.toInt()))
        }
    }

    fun part1(input: String, space: Space): Long {
        val robots = parse(input)

        repeat(100) {
            for (robot in robots)
                robot.move(space)
        }

        val a = robots.groupBy { space.quadrantOf(it.p) }
        return a[1]!!.size.toLong() * a[2]!!.size.toLong() * a[3]!!.size.toLong() * a[4]!!.size.toLong()
    }

    fun isTree(robots: List<Robot>): Boolean {
        // search for a 3x3 clump of points
        val points = robots.map { it.p }.toSet()
        return points.any { p ->  p.neighbors.all { n -> n in points }}
    }

    fun part2(input: String): Int {
        val robots = parse(input)

        repeat(Int.MAX_VALUE) { seconds ->
            if (isTree(robots))
                return seconds

            for (robot in robots)
                robot.move()
        }

        error("tree not found")
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput, Space.test) == 12L)

    val input = readInput("Day14")
    part1(input, Space.real).println()
    part2(input).println()
}
