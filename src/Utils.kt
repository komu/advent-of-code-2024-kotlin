import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.log10

fun readInput(name: String) = Path("src/$name.txt").readText().trim()

fun readInputLines(name: String) = readInput(name).lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

enum class Direction(val dx: Int, val dy: Int) {
    N(0, -1), S(0, 1), W(-1, 0), E(1, 0), NW(-1, -1), NE(-1, 1), SW(1, -1), SE(1, 1);

    fun toVec() = Vec2(dx, dy)
}

enum class CardinalDirection(val dx: Int, val dy: Int) {
    N(0, -1), S(0, 1), W(-1, 0), E(1, 0);

    fun clockwise(): CardinalDirection = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    fun toVec() = Vec2(dx, dy)
}

data class Vec2(val x: Int, val y: Int) {
    fun squaredMagnitude() = x * x + y * y
}

operator fun Int.times(v: Vec2) = Vec2(this * v.x, this * v.y)
operator fun Int.times(d: Direction) = this * d.toVec()

data class Point(val x: Int, val y: Int) {
    operator fun plus(v: Vec2) = Point(x + v.x, y + v.y)
    operator fun minus(v: Vec2) = Point(x - v.x, y - v.y)
    operator fun plus(d: Direction) = this + d.toVec()
    operator fun plus(d: CardinalDirection) = this + d.toVec()
    operator fun minus(p: Point) = Vec2(x - p.x, y - p.y)

    fun isCardinalNeighbor(p: Point) =
        (this - p).squaredMagnitude() == 1
}

data class Bounds(val xRange: IntRange, val yRange: IntRange) {
    operator fun contains(p: Point) = p.y in yRange && p.x in xRange
}

fun Long.concat(y: Long): Long {
    var xx = this
    var yy = y
    while (yy > 0) {
        yy /= 10
        xx *= 10
    }
    return xx + y
}

fun <T> List<T>.choosePairs(): List<Pair<T, T>> =
    withIndex().flatMap { (index, a) -> subList(index + 1, size).map { b -> a to b } }

fun <T> Collection<T>.choosePairs(): List<Pair<T, T>> =
    toList().choosePairs()

val IntRange.size: Int
    get() = if (isEmpty()) 0 else last - first + 1

fun sum(range: IntRange): Int =
    sumFromZeroTo(range.endInclusive) - sumFromZeroTo(range.start - 1)

private fun sumFromZeroTo(n: Int) =
    ((n.toLong() * (n + 1)) / 2).toInt()

fun countDigits(n: Long): Int {
    require(n >= 0)
    if (n == 0L) return 1
    return (log10(n.toDouble()) + 1).toInt()
}

fun <T> T.trace(prefix: String? = null): T {
    if (prefix != null)
        print("$prefix: ")
    println(this)
    return this
}

fun valuesWithPoints(grid: List<String>): List<Pair<Point, Char>> =
    grid.withIndex().flatMap { (y, row) -> row.withIndex().map { (x, c) -> Point(x, y) to c } }

fun floodFillCardinal(initial: Point, accept: (Point) -> Boolean): Set<Point> {
    val result = mutableSetOf<Point>(initial)

    fun recurse(p: Point) {
        for (d in CardinalDirection.entries) {
            val n = p + d
            if (accept(n) && result.add(n))
                recurse(n)
        }
    }

    recurse(initial)
    return result
}
