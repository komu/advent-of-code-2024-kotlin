private class VM(var a: Long, var b: Long, var c: Long, val program: List<Int>) {
    var ip = 0
    val output = mutableListOf<Int>()

    val running: Boolean
        get() = ip in program.indices

    fun step(a: Long): Int {
        this.a = a
        ip = 0
        runUntilNextBranch()
        return output.last()
    }

    fun runUntilNextBranch() {
        while (running) {
            val inst = program[ip]
            val operand = program[ip + 1]

            when (inst) {
                0 -> a = a / (1 shl combo(operand).toInt())
                1 -> b = b xor operand.toLong()
                2 -> b = combo(operand) % 8
                3 -> if (a != 0L) {
                    ip = operand.toInt()
                    return
                }
                4 -> b = b xor c
                5 -> output.add((combo(operand) % 8).toInt())
                6 -> b = a / (1 shl combo(operand).toInt())
                7 -> c = a / (1 shl combo(operand).toInt())
                else -> error("invalid $inst")
            }
            ip += 2
        }
    }

    private fun combo(i: Int): Long = when (i) {
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
            vm.runUntilNextBranch()

        return vm.output.joinToString(",")
    }

    fun part2(input: String): Long {
        val vm = parse(input)

        var a = 0L
        for (op in vm.program.reversed()) {
            a *= 8
            a = (a..a + 255).find { vm.step(it) == op }!!
        }

        return a
    }

    check(part1(readInput("Day17_test")) == "4,6,3,5,6,3,5,2,1,0")
    check(part2(readInput("Day17_test")) == 29328L)
    check(part2(readInput("Day17_test2")) == 117440L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
