private class VM(var a: Long, var b: Long, var c: Long, val program: List<Int>) {
    var ip = 0
    val output = mutableListOf<Int>()

    val running: Boolean
        get() = ip in program.indices

    fun runUntilNextOutput() {
        while (running) {
            val inst = program[ip]
            val operand = program[ip + 1]
            ip += 2

            when (inst) {
                0 -> a = a / pow2(combo(operand))
                1 -> b = b xor operand
                2 -> b = combo(operand) % 8
                3 -> if (a != 0L) ip = operand
                4 -> b = b xor c
                5 -> {
                    output.add((combo(operand) % 8).toInt())
                    return
                }

                6 -> b = a / pow2(combo(operand))
                7 -> c = a / pow2(combo(operand))
                else -> error("invalid $inst")
            }
        }
    }

    private fun combo(i: Int) = when (i) {
        in 0..3 -> i.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> error("invalid $i")
    }
}

fun main() {

    fun parse(input: String): VM {
        val lines = input.lines()
        return VM(
            a = lines[0].removePrefix("Register A: ").toLong(),
            b = lines[1].removePrefix("Register B: ").toLong(),
            c = lines[2].removePrefix("Register C: ").toLong(),
            program = lines[4].removePrefix("Program: ").split(",").map { it.toInt() }
        )
    }

    fun part1(input: String): String {
        val vm = parse(input)

        while (vm.running)
            vm.runUntilNextOutput()

        return vm.output.joinToString(",")
    }

    fun part2(input: String): Long {
        val vm = parse(input)

        return vm.program.foldRight(0L) { op, a ->
            ((8 * a)..(8 * a) + 255).find { candidate ->
                vm.a = candidate
                vm.ip = 0
                vm.runUntilNextOutput()
                vm.output.last() == op
            }!!
        }
    }

    check(part1(readInput("Day17_test")) == "4,6,3,5,6,3,5,2,1,0")
    check(part2(readInput("Day17_test")) == 29328L)
    check(part2(readInput("Day17_test2")) == 117440L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
