import CardinalDirection.E

private class Maze(input: String) {
    val grid = CharGrid(input)
    val start = grid.find('S') ?: error("no start")
    val end = grid.find('E') ?: error("no end")
    val startState = MazeState(start, E)

    fun transitions(state: MazeState): List<Pair<MazeState, Int>> {
        return listOf(
            MazeState(state.point + state.direction, state.direction) to 1,
            MazeState(state.point, state.direction.clockwise()) to 1000,
            MazeState(state.point, state.direction.counterClockwise()) to 1000,
        ).filter { (s, _) -> grid[s.point] != '#' }
    }

    fun isEnd(state: MazeState) = state.point == end
}

private data class MazeState(val point: Point, val direction: CardinalDirection)

private class ChainLink(val state: MazeState, val previous: ChainLink?)

fun main() {

    fun part1(input: String): Int {
        val maze = Maze(input)
        return shortestPathCost(MazeState(maze.start, E), { it.point == maze.end }, maze::transitions)
    }

    fun part2(input: String): Int {
        val maze = Maze(input)

        val max = shortestPathCost(maze.startState, maze::isEnd, maze::transitions)

        val seenPoints = mutableSetOf<Point>()

        val stack = mutableListOf(ChainLink(maze.startState, null))
        var costs = mutableMapOf(maze.startState to 0)

        while (stack.isNotEmpty()) {
            val link = stack.removeFirst()
            val state = link.state
            val costToState = costs[state]!!
            if (costToState > max) continue

            if (maze.isEnd(state)) {
                var l: ChainLink? = link
                while (l != null) {
                    seenPoints.add(l.state.point)
                    l = l.previous
                }
            }

            for ((s, c) in maze.transitions(state)) {
                val nextCost = costToState + c
                if (nextCost <= costs.getOrDefault(s, Int.MAX_VALUE)) {
                    costs[s] = nextCost
                    stack.add(ChainLink(s, link))
                }
            }
        }

        return seenPoints.size
    }

    check(part1(readInput("Day16_test")) == 7036)
    check(part1(readInput("Day16_test2")) == 11048)
    check(part2(readInput("Day16_test")) == 45)
    check(part2(readInput("Day16_test2")) == 64)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

