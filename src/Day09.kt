import kotlin.time.measureTime

private fun defrag1(buffer: MutableList<Int?>): Long {
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

private fun defrag2(files: List<File>, frees: MutableList<Free>): Long {
    var cleanupCounter = 0

    return files.asReversed().sumOf { file ->
        val free = frees.findFirstFree(file)

        val targetOffset = free?.offset ?: file.offset

        if (free != null) {
            free.consume(file.blocks)
            if (free.blocks == 0)
                cleanupCounter++
        }

        if (cleanupCounter >= 250) {
            frees.removeIf { it.blocks == 0 }
            cleanupCounter = 0
        }

        file.id.toLong() * sum(targetOffset..<targetOffset + file.blocks)
    }
}

private fun List<Free>.findFirstFree(file: File): Free? {
    // Explicit loop improves 20% over:
    //    asSequence().takeWhile { it.offset < file.offset }.find { it.blocks >= file.blocks }
    for (free in this) {
        if (free.offset >= file.offset)
            break
        if (free.blocks >= file.blocks)
            return free
    }
    return null
}

private class File(val id: Int, val offset: Int, val blocks: Int)

private class Free(var offset: Int, var blocks: Int) {
    fun consume(blocks: Int) {
        this.offset += blocks
        this.blocks -= blocks
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

        return defrag1(buffer)
    }

    fun part2(input: String): Long {
        var offset = 0
        val files = mutableListOf<File>()
        val frees = mutableListOf<Free>()

        for ((id, chunk) in input.map { it.digitToInt() }.chunked(2).withIndex()) {
            val fileSize = chunk[0]
            val freeSize = chunk.getOrNull(1) ?: 0

            files += File(id, offset, fileSize)
            if (freeSize != 0)
                frees += Free(offset + fileSize, freeSize)

            offset += fileSize + freeSize
        }

        return defrag2(files, frees)
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
