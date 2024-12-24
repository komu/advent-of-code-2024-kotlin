private data class Gate(val lhs: String, val op: String, val rhs: String, val out: String)

private fun String.isInputWire() = startsWith('x') || startsWith('y')
private fun String.isOutputWire() = startsWith('z')

private fun evaluateCircuit(gates: Map<String, Gate>, input: Map<String, Boolean>): Long {
    val wireValues = input.toMutableMap()

    fun evaluateWire(wire: String): Boolean = wireValues.getOrPut(wire) {
        val gate = gates[wire] ?: error("no value for $wire")

        val lhs = evaluateWire(gate.lhs)
        val rhs = evaluateWire(gate.rhs)

        when (gate.op) {
            "AND" -> lhs && rhs
            "OR" -> lhs || rhs
            "XOR" -> lhs xor rhs
            else -> error("invalid op ${gate.op}")
        }
    }

    var value = 0L

    for (z in gates.keys.filter { it.isOutputWire() }.sortedDescending()) {
        value = value shl 1
        if (evaluateWire(z))
            value = value or 1
    }

    return value
}

// The problem talks about detecting which pairs need to be swapped, but that's misleading,
// since the output is just a sorted list of wires. We don't even need to detect the pairs,
// just wires that are bad.
private fun findBadWires(gates: Map<String, Gate>): List<String> {
    fun Gate.isWiredToInputs() = lhs.isInputWire() && rhs.isInputWire()

    val bad = mutableListOf<String>()

    for (gate in gates.values) {
        val lhsOp = gates[gate.lhs]?.op
        val rhsOp = gates[gate.rhs]?.op

        if (gate.out in listOf("z01", "z45"))
            continue

        if (gate.out.isOutputWire()) {
            when {
                gate.op != "XOR" -> bad.add(gate.out)
                lhsOp == "AND" -> bad.add(gate.lhs)
                rhsOp == "AND" -> bad.add(gate.rhs)
                lhsOp == "XOR" && rhsOp == "XOR" -> when {
                    gates[gate.lhs]?.isWiredToInputs() == false -> bad.add(gate.lhs)
                    gates[gate.rhs]?.isWiredToInputs() == false -> bad.add(gate.rhs)
                    else -> error("bad output with unexpected form: ${gate.out}")
                }
            }

        } else if (gate.op == "OR") {
            if (lhsOp != "AND") bad.add(gate.lhs)
            if (rhsOp != "AND") bad.add(gate.rhs)
        }
    }

    return bad
}

private fun generateDot(gates: Map<String, Gate>): String = buildString {
    appendLine("digraph G {")

    for (wire in gates.values.flatMap { listOf(it.lhs, it.rhs) }.distinct())
        if (wire.isInputWire())
            appendLine("$wire [label=\"${wire}\", shape=square];")

    for (gate in gates.values) {
        val color = when (gate.op) {
            "AND" -> "red"
            "OR" -> "green"
            "XOR" -> "yellow"
            else -> error("invalid op ${gate.op}")
        }

        appendLine("${gate.lhs} -> ${gate.out};")
        appendLine("${gate.rhs} -> ${gate.out};")
        appendLine("${gate.out} [label=\"${gate.op} ${gate.out}\", fillcolor=$color, style=filled];")

        if (gate.out.isOutputWire()) {
            appendLine("${gate.out} -> ${gate.out}out;")
            appendLine("${gate.out}out [label=\"${gate.out}\", shape=square];")
        }
    }

    appendLine("}")
}

fun main(args: Array<String>) {

    fun parse(input: String, swaps: List<Pair<String, String>>? = null): Pair<Map<String, Gate>, Map<String, Boolean>> {
        val (p1, p2) = input.split("\n\n")
        val hardwires = mutableMapOf<String, Boolean>()

        for (p in p1.lines()) {
            val (g, v) = p.split(": ")

            hardwires[g] = v.toInt() == 1
        }

        val fixMap = swaps?.let { it.toMap() + it.associate { (w1, w2) -> w2 to w1 } }

        val gates = p2.lines().associate { p ->
            val (lhs, op, rhs, _, o) = p.split(" ")
            val out = fixMap?.get(o) ?: o
            out to Gate(lhs, op, rhs, out)
        }

        return Pair(gates, hardwires)
    }

    fun part1(input: String): Long {
        val (circuit, hardwires) = parse(input)
        return evaluateCircuit(circuit, hardwires)
    }

    fun part2(input: String): String {
        val (circuit, _) = parse(input)
        return findBadWires(circuit).sorted().joinToString(",")
    }

    if (args.size == 1 && args[0] == "dot") {
        val (circuit, _) = parse(readInput("Day24"))
        println(generateDot(circuit))
        return
    }

    check(part1(readInput("Day24_test")) == 4L)
    check(part1(readInput("Day24_test2")) == 2024L)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}

