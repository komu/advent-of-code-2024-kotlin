#set( $Code = "bar" )

private data class Row(val x: Int, val y: Int) {
    
    companion object {
        private val regex = Regex("""(\d+)\s+(\d+)""")
        
        fun parse(s: String): Row {
            val (x, y) = regex.matchEntire(s)?.destructured ?: error("invalid input line '\$s'")
            return Row(x.toInt(), y.toInt()) 
        }
    }
}

fun main() {
    fun part1(input: String): Int {
        val rows = input.lines().map { Row.parse(it) }
        println(rows)
        return 0
    }

    fun part2(input: String): Int {
        return 0
    }

    val testInput = readInput("Day${Day}_test")
    val p1 = part1(testInput)
    p1.println() 
    check(p1 == 0)
    val p2 = part2(testInput)
    p2.println() 
    //check(p2 == 0)

    println("--")
    val input = readInput("Day$Day")
    part1(input).println()
    part2(input).println()
}
