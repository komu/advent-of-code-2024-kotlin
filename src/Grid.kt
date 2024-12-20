import java.util.*

abstract class Grid<T>(val width: Int, val height: Int) {

    val xRange = 0..<width
    val yRange = 0..<height

    val points: Set<Point> by lazy {
        buildSet {
            for (y in yRange)
                for (x in xRange)
                    add(Point(x, y))
        }
    }

    val pointsWithValues: Sequence<Pair<Point, T>>
        get() = points.asSequence().map { it to getRequired(it) }

    val distinctValues: Set<T>
        get() = points.mapTo(mutableSetOf(), this::getRequired)

    fun find(value: T): Point? =
        pointsWithValues.firstOrNull { (_, v) -> v == value }?.first

    fun findAll(value: T): Set<Point> =
        points.filterTo(mutableSetOf<Point>()) { getRequired(it) == value }

    fun getRequired(p: Point): T = this[p] ?: throw IllegalArgumentException("$p is out of bounds")
    open operator fun get(p: Point): T? = if (p in this) getChecked(p.x, p.y) else null
    open operator fun get(x: Int, y: Int): T? = if (contains(x, y)) getChecked(x, y) else null

    operator fun contains(p: Point) = contains(p.x, p.y)
    fun contains(x: Int, y: Int) = x in xRange && y in yRange

    protected abstract fun getChecked(x: Int, y: Int): T
}

abstract class MutableGrid<T>(width: Int, height: Int) : Grid<T>(width, height) {
    operator fun set(p: Point, value: T) {
        require(p in this) { throw IllegalArgumentException("$p is out of bounds") }

        set(p.x, p.y, value)
    }

    protected abstract fun set(x: Int, y: Int, value: T)

    inline fun flood(initial: Point, value: T, neighbors: (Point) -> Iterable<Point>, accept: (Point) -> Boolean) {
        val stack = mutableListOf(initial)

        while (stack.isNotEmpty()) {
            val p = stack.removeLast()
            if (accept(p)) {
                this[p] = value
                stack += neighbors(p)
            }
        }
    }
}

class CharGrid(private val data: String, width: Int) : Grid<Char>(width, data.length / width) {

    // add 1 to ignore ending newlines
    private fun offset(x: Int, y: Int) = y * (width + 1) + x

    override fun getChecked(x: Int, y: Int) = data[offset(x, y)]

    fun distancesFrom(start: Point, accept: (Char) -> Boolean): IntGrid {
        val costs = IntGrid(width, height, Int.MAX_VALUE)
        costs[start] = 0

        val queue = ArrayDeque<Point>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val u = queue.removeFirst()
            val cost = costs[u]

            for (n in u.cardinalNeighbors) {
                if (accept(getRequired(n)) && costs[n] == Int.MAX_VALUE) {
                    costs[n] = cost + 1
                    queue.push(n)
                }
            }
        }

        return costs
    }

    companion object {
        operator fun invoke(input: String): CharGrid {
            val width = input.indexOf('\n')
            return CharGrid(input, width)
        }
    }
}

class IntGrid private constructor(private val data: IntArray, width: Int) : MutableGrid<Int>(width, data.size / width) {

    constructor(width: Int, height: Int, initialValue: Int = 0) : this(IntArray(width * height) { initialValue }, width)

    private fun offset(x: Int, y: Int): Int = y * width + x

    override operator fun get(p: Point): Int = data[offset(p.x, p.y)]
    override fun getChecked(x: Int, y: Int): Int = data[offset(x, y)]

    override fun set(x: Int, y: Int, value: Int) {
        data[offset(x, y)] = value
    }
}

class MutableCharGrid private constructor(private val data: CharArray, width: Int) : MutableGrid<Char>(width, data.size / width) {

    private fun offset(x: Int, y: Int): Int = y * width + x

    override fun getChecked(x: Int, y: Int) = data[offset(x, y)]

    override fun set(x: Int, y: Int, value: Char) {
        data[offset(x, y)] = value
    }

    companion object {
        operator fun invoke(input: String): MutableCharGrid {
            val lines = input.lines()
            val w = lines.first().length
            val h = lines.size

            val array = CharArray(w * h)
            for ((y, line) in lines.withIndex())
                for ((x, c) in line.withIndex())
                    array[y * w + x] = c

            return MutableCharGrid(array, w)
        }
    }
}
