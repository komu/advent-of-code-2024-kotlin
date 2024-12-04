import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

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
    N(-1, 0), S(1, 0), W(0, -1), E(0, 1), NW(-1, -1), NE(1, -1), SW(-1, 1), SE(1, 1);

    fun toVec() = Vec2(dx, dy)
}

class Vec2(val x: Int, val y: Int)

operator fun Int.times(v: Vec2) = Vec2(this * v.x, this * v.y)
operator fun Int.times(d: Direction) = this * d.toVec()

data class Point(val x: Int, val y: Int) {
    operator fun plus(v: Vec2) = Point(x + v.x, y + v.y)
    operator fun plus(d: Direction) = this + d.toVec()
}
