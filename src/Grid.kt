abstract class Grid<T>(protected val width: Int, height: Int) {

    val xRange = 0..<width
    val yRange = 0..<height

    val points: Set<Point> by lazy {
        buildSet {
            for (y in yRange)
                for (x in xRange)
                    add(Point(x, y))
        }
    }

    fun find(value: T): Point? = points.find { this[it] == value }

    fun findAll(value: T): Set<Point> = points.filterTo(mutableSetOf<Point>()) { this[it] == value }

    open operator fun get(p: Point): T? = if (p in this) get(p.x, p.y) else null
    fun get(p: Point, defaultValue: T): T = get(p) ?: defaultValue
    operator fun contains(p: Point) = p.x in xRange && p.y in yRange

    protected abstract fun get(x: Int, y: Int): T
}

class CharGrid(private val data: String, private val default: Char, width: Int, height: Int) : Grid<Char>(width, height) {

    // add 1 to ignore ending newlines
    private fun offset(x: Int, y: Int): Int = y * (width + 1) + x

    override operator fun get(p: Point): Char = super.get(p) ?: default
    override fun get(x: Int, y: Int): Char = data[offset(x, y)]

    companion object {
        operator fun invoke(input: String, default: Char = '\u0000'): CharGrid {
            val width = input.indexOf('\n')
            return CharGrid(input, default, width, input.length / width)
        }
    }
}
