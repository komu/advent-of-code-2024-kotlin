import java.util.*

data class PathResult<T>(val cost: Int, val result: List<T>)
data class PathResultLong<T>(val cost: Long, val result: List<T>)
data class PathsResult<T>(val cost: Int, val paths: List<List<T>>)

fun <T> shortestPathWithUniformCost(from: T, target: T, edges: (T) -> List<T>): PathResult<T>? =
    shortestPathWithUniformCost(from, { it == target }, edges)

fun <T> shortestPathWithUniformCost(from: T, isTarget: (T) -> Boolean, edges: (T) -> List<T>): PathResult<T>? {
    val initial = PathNode(from, null, 0)
    val seen = mutableSetOf<T>()

    val queue = ArrayDeque<PathNode<T>>()
    queue.add(initial)

    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()

        if (isTarget(u.point)) {
            return PathResult(u.totalCost, u.toList())
        } else {
            for (v in edges(u.point))
                if (seen.add(v))
                    queue += PathNode(v, u, u.totalCost + 1)
        }
    }

    return null
}

fun <T> shortestPath(from: T, target: T, edges: (T) -> List<Pair<T, Int>>): PathResult<T>? =
    shortestPath(from, { it == target }, edges)

fun <T> shortestPath(from: T, isTarget: (T) -> Boolean, edges: (T) -> List<Pair<T, Int>>): PathResult<T>? {
    val initial = PathNode(from, null, 0)
    val queue = PriorityQueue(setOf(initial))
    var costs = mutableMapOf(from to 0)

    while (queue.isNotEmpty()) {
        val u = queue.remove()

        if (isTarget(u.point)) {
            return PathResult(u.totalCost, u.toList())
        } else {
            for ((v, cost) in edges(u.point)) {
                val newCost = u.totalCost + cost

                if (newCost < costs.getOrDefault(v, Int.MAX_VALUE)) {
                    costs[v] = newCost
                    queue += PathNode(v, u, newCost)
                }
            }
        }
    }

    return null
}

fun <T> shortestPathLong(from: T, isTarget: (T) -> Boolean, edges: (T) -> List<Pair<T, Long>>): PathResultLong<T>? {
    val initial = PathNodeLong(from, null, 0)
    val queue = PriorityQueue(setOf(initial))
    var costs = mutableMapOf(from to 0L)

    while (queue.isNotEmpty()) {
        val u = queue.remove()

        if (isTarget(u.point)) {
            return PathResultLong(u.totalCost, u.toList())
        } else {
            for ((v, cost) in edges(u.point)) {
                val newCost = u.totalCost + cost

                if (newCost < costs.getOrDefault(v, Long.MAX_VALUE)) {
                    costs[v] = newCost
                    queue += PathNodeLong(v, u, newCost)
                }
            }
        }
    }

    return null
}

fun <T> allShortestPaths(from: T, target: T, edges: (T) -> List<Pair<T, Int>>): PathsResult<T> =
    allShortestPaths(from, { it == target }, edges)

fun <T> allShortestPaths(from: T, isTarget: (T) -> Boolean, edges: (T) -> List<Pair<T, Int>>): PathsResult<T> {
    val initial = PathNode(from, null, 0)
    val queue = PriorityQueue(setOf(initial))
    val paths = mutableListOf<List<T>>()
    var costs = mutableMapOf(from to 0)
    var best = Int.MAX_VALUE

    while (queue.isNotEmpty()) {
        val u = queue.remove()
        if (u.totalCost > best)
            continue

        if (isTarget(u.point)) {
            best = u.totalCost
            paths += u.toList()

        } else {
            for ((v, cost) in edges(u.point)) {
                val newCost = u.totalCost + cost

                if (newCost <= costs.getOrDefault(v, Int.MAX_VALUE)) {
                    costs[v] = newCost
                    queue += PathNode(v, u, newCost)
                }
            }
        }
    }

    return PathsResult(best, paths)
}

private class PathNode<T>(val point: T, val previous: PathNode<T>?, val totalCost: Int) : Comparable<PathNode<T>> {
    override fun compareTo(other: PathNode<T>) = totalCost.compareTo(other.totalCost)

    fun toList() = buildList {
        var node: PathNode<T>? = this@PathNode
        while (node != null) {
            add(node.point)
            node = node.previous
        }
        reverse()
    }
}

private class PathNodeLong<T>(val point: T, val previous: PathNodeLong<T>?, val totalCost: Long) : Comparable<PathNodeLong<T>> {
    override fun compareTo(other: PathNodeLong<T>) = totalCost.compareTo(other.totalCost)

    fun toList() = buildList {
        var node: PathNodeLong<T>? = this@PathNodeLong
        while (node != null) {
            add(node.point)
            node = node.previous
        }
        reverse()
    }
}
