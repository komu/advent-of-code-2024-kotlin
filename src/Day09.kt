private class Buffer(input: String) {
    val buffer = mutableListOf<Int?>()

    init {
        for ((id, chunk) in input.toList().chunked(2).withIndex()) {
            buffer += List(chunk[0].digitToInt()) { id }
            buffer += List(chunk.getOrNull(1)?.digitToInt() ?: 0) { null }
        }
    }

    fun defrag1() {
        val emptyIndices = buffer.withIndex()
            .mapNotNull { (i, v) -> i.takeIf { v == null } }
            .asReversed()
            .toMutableList()

        while (emptyIndices.last() < buffer.size) {
            val last = buffer.removeLast() ?: continue
            buffer[emptyIndices.removeLast()] = last
        }
    }

    fun defrag2() {
        var index = buffer.lastIndex
        while (index > 0) {
            if (buffer[index] == null) {
                index--
            } else {
                val block = findBlock(index)

                val freeIndex = findFreeBlockBefore(block)
                if (freeIndex != null) {
                    buffer.copy(block, freeIndex)
                    buffer.set(block, null)
                }

                index -= block.size
            }
        }
    }

    fun findFreeBlockBefore(block: IntRange) =
        (0..<block.first).find { start ->
            start + block.size < buffer.size && (0..<block.size).all { offset -> buffer[start + offset] == null }
        }

    fun findBlock(end: Int): IntRange {
        val c = buffer[end] ?: error("no value at $end")

        var start = end
        while (start > 0 && buffer[start - 1] == c)
            start--

        return start..end
    }

    fun checksum() = buffer.withIndex().sumOf { (i, s) -> i.toLong() * (s?.toLong() ?: 0) }
}


fun main() {
    fun part1(input: String): Long {
        val buffer = Buffer(input)
        buffer.defrag1()
        return buffer.checksum()
    }

    fun part2(input: String): Long {
        val buffer = Buffer(input)
        buffer.defrag2()
        return buffer.checksum()
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
