private class Maze(input: String) {
    val grid = CharGrid(input)
    private val start = grid.find('S')!!
    private val end = grid.find('E')!!

    val startState = MazeState(start, CardinalDirection.E)

    fun isEnd(state: MazeState) = state.p == end

    fun transitions(state: MazeState) = buildList {
        val forward = state.p + state.d
        if (grid[forward] != '#')
            add(MazeState(forward, state.d) to 1)

        add(MazeState(state.p, state.d.clockwise()) to 1000)
        add(MazeState(state.p, state.d.counterClockwise()) to 1000)
    }

    data class MazeState(val p: Point, val d: CardinalDirection)
}

fun main() {
    fun part1(input: String): Int {
        val maze = Maze(input)
        return shortestPath(maze.startState, maze::isEnd, maze::transitions)?.cost ?: error("No path found")
    }

    fun part2(input: String): Int {
        val maze = Maze(input)
        val result = allShortestPaths(maze.startState, maze::isEnd, maze::transitions)
        return result.paths.flatten().map { it.p }.toSet().size
    }

    check(part1(readInput("Day16_test")) == 7036)
    check(part1(readInput("Day16_test2")) == 11048)
    check(part2(readInput("Day16_test2")) == 64)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
