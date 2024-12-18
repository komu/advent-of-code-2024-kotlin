#set( $Code = "bar" )

private data class Row(val x: Int, val y: Int)

fun main() {
    val regex = Regex("""(\d+)\s+(\d+)""")
    
    fun parse(input: String) =
        input.lines().map {
            val (x, y) = regex.matchEntire(it)?.destructured ?: error("invalid input line '\$it'")
            Row(x.toInt(), y.toInt())
        }
    
    fun part1(input: String): Int {
        val rows = parse(input)
        println(rows)
        return 0
    }

    fun part2(input: String): Int {
        return 0
    }

    check(part1(readInput("Day18_test")).trace("part 1") == 0)
    //check(part2(readInput("Day18_test")).trace("part ") == 0)

    println("--")
    val input = readInput("Day$Day")
    part1(input).println()
    part2(input).println()
}
