import Arrow.*
import Num.*

private enum class Num {
    N0, N1, N2, N3, N4, N5, N6, N7, N8, N9, NA;
}

private enum class Arrow {
    AL, AU, AD, AR, AA,
}

/** Transition on T, performing a single lower level action A */
private data class Transition<T, A>(val target: T, val action: A)

/** Valid transitions between elements of T, actions being elements of A */
private class Transitions<T, A>(val transitions: Map<T, List<Transition<T, A>>>) {

    val buttons: Set<T>
        get() = transitions.keys

    operator fun get(c: T) = transitions[c] ?: error("no transitions for $c in ${transitions.keys}")
}

private infix fun <T, A> T.byPressing(action: A): Transition<T, A> = Transition(this, action)

private val arrowTransitions = Transitions<Arrow, Arrow>(
    transitions = mapOf(
        AA to listOf(AU byPressing AL, AR byPressing AD),
        AU to listOf(AA byPressing AR, AD byPressing AD),
        AL to listOf(AD byPressing AR),
        AD to listOf(AL byPressing AL, AU byPressing AU, AR byPressing AR),
        AR to listOf(AD byPressing AL, AA byPressing AU),
    )
)

private val numpadTransitions = Transitions<Num, Arrow>(
    transitions = mapOf(
        NA to listOf(N0 byPressing AL, N3 byPressing AU),
        N0 to listOf(NA byPressing AR, N2 byPressing AU),
        N1 to listOf(N4 byPressing AU, N2 byPressing AR),
        N2 to listOf(N1 byPressing AL, N5 byPressing AU, N3 byPressing AR, N0 byPressing AD),
        N3 to listOf(N2 byPressing AL, N6 byPressing AU, NA byPressing AD),
        N4 to listOf(N7 byPressing AU, N5 byPressing AR, N1 byPressing AD),
        N5 to listOf(N4 byPressing AL, N8 byPressing AU, N6 byPressing AR, N2 byPressing AD),
        N6 to listOf(N5 byPressing AL, N9 byPressing AU, N3 byPressing AD),
        N7 to listOf(N8 byPressing AR, N4 byPressing AD),
        N8 to listOf(N7 byPressing AL, N9 byPressing AR, N5 byPressing AD),
        N9 to listOf(N8 byPressing AL, N6 byPressing AD),
    )
)

private class Costs<T>(private val pressCosts: Map<Pair<T, T>, Long>) {
    fun pressCost(a: T, b: T) = pressCosts[a to b] ?: error("no cost for $a,$b")

    fun <U> next(transitions: Transitions<U, T>, buttonA: T): Costs<U> {
        val newCosts = mutableMapOf<Pair<U, U>, Long>()

        for (a in transitions.buttons)
            for (b in transitions.buttons)
                newCosts[a to b] = calculatePressCost(a, b, transitions, buttonA)

        return Costs(newCosts)
    }

    private fun <U> calculatePressCost(start: U, buttonToPress: U, transitions: Transitions<U, T>, buttonA: T): Long {
        data class State(val location: U, val lastAction: T, val pressed: Boolean = false)

        val initialState = State(location = start, buttonA)
        return shortestPathLong(initialState, { it.pressed }) { state ->
            if (state.location == buttonToPress) {
                listOf(State(state.location, state.lastAction, pressed = true) to pressCost(state.lastAction, buttonA))
            } else {
                transitions[state.location].map { tr ->
                    State(tr.target, tr.action) to pressCost(state.lastAction, tr.action)
                }
            }
        }?.cost ?: error("no cost")
    }

    companion object {
        fun initial() = Costs(buildMap {
            for (a in Arrow.entries)
                for (b in Arrow.entries)
                    put(a to b, 1)
        })
    }
}

fun main() {

    fun createCosts(steps: Int): Costs<Num> {
        var costs = Costs.initial()

        repeat(steps) {
            costs = costs.next(arrowTransitions, AA)
        }

        return costs.next(numpadTransitions, AA)
    }

    fun complexity(code: String, steps: Int): Long {
        val costs = createCosts(steps)
        var previous = NA
        var pathLength = 0L

        for (button in code.toList().map { Num.valueOf("N$it") }) {
            pathLength += costs.pressCost(previous, button)
            previous = button
        }

        return code.take(3).toLong() * pathLength
    }

    fun part1(input: String) = input.lines().sumOf { complexity(it, 2) }
    fun part2(input: String) = input.lines().sumOf { complexity(it, 25) }

    check(part1(readInput("Day21_test")) == 126384L)

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}
