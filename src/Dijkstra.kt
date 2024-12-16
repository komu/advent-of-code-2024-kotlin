import java.util.*

data class PathResult<T>(val cost: Int, val result: List<T>)
data class PathsResult<T>(val cost: Int, val paths: List<List<T>>)

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
