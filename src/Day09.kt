private class SegmentBuffer(input: String) {
    val files = mutableListOf<File>()
    val frees = mutableListOf<Free>()

    init {
        var offset = 0
        for ((id, chunk) in input.map { it.digitToInt() }.chunked(2).withIndex()) {
            val file = File(id, offset, chunk[0])
            files += file
            offset += file.blocks
            if (chunk.size == 2 && chunk[1] != 0) {
                val free = Free(offset, chunk[1])
                frees += free
                offset += free.blocks
            }
        }
    }

    fun defrag(): Long {
        var checksum = 0L

        val cleanupRounds = 250
        var cleanupCounter = cleanupRounds

        for (file in files.asReversed()) {
            val free = frees.asSequence().takeWhile { it.offset < file.offset }.find { it.blocks >= file.blocks }
            checksum += checksum(file.id, free?.offset ?: file.offset, file.blocks)

            free?.consume(file.blocks)

            if (--cleanupCounter <= 0) {
                frees.removeIf { it.blocks == 0 }
                cleanupCounter = cleanupRounds
            }
        }

        return checksum
    }

    fun checksum(id: Int, offset: Int, blocks: Int) = id.toLong() * sum(offset..<offset + blocks)

    class File(val id: Int, val offset: Int, val blocks: Int)

    class Free(var offset: Int, var blocks: Int) {
        fun consume(blocks: Int) {
            this.offset += blocks
            this.blocks -= blocks
        }
    }
}


fun main() {
    fun part1(input: String): Long {
        val buffer = mutableListOf<Int?>().apply {
            for ((id, chunk) in input.map { it.digitToInt() }.chunked(2).withIndex()) {
                addAll(List(chunk[0]) { id })
                addAll(List(chunk.getOrNull(1) ?: 0) { null })
            }
        }

        val emptyIndices = buffer.withIndex()
            .mapNotNull { (i, v) -> i.takeIf { v == null } }
            .asReversed()
            .toMutableList()

        while (emptyIndices.last() < buffer.size) {
            val last = buffer.removeLast() ?: continue
            buffer[emptyIndices.removeLast()] = last
        }

        return buffer.withIndex().sumOf { (i, s) -> i.toLong() * (s?.toLong() ?: 0) }
    }

    fun part2(input: String) = SegmentBuffer(input).defrag()

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
