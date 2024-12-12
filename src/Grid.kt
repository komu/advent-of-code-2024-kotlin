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

    private fun getRequired(p: Point): T = this[p] ?: throw IllegalArgumentException("$p is out of bounds")
    operator fun get(p: Point): T? = if (p in this) get(p.x, p.y) else null
    operator fun contains(p: Point) = p.x in xRange && p.y in yRange

    protected abstract fun get(x: Int, y: Int): T
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

    override fun get(x: Int, y: Int) = data[offset(x, y)]

    companion object {
        operator fun invoke(input: String): CharGrid {
            val width = input.indexOf('\n')
            return CharGrid(input, width)
        }
    }
}

class IntGrid(private val data: IntArray, width: Int) : MutableGrid<Int>(width, data.size / width) {

    constructor(width: Int, height: Int, initialValue: Int = 0) : this(IntArray(width * height) { initialValue }, width)

    private fun offset(x: Int, y: Int): Int = y * width + x

    override fun get(x: Int, y: Int) = data[offset(x, y)]

    override fun set(x: Int, y: Int, value: Int) {
        data[offset(x, y)] = value
    }
}
