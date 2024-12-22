fun main() {
    fun nextSecret(x: Int): Int {
        val mod = 16777216L

        var x = x.toLong()
        x = (x xor (x * 64)) % mod
        x = (x xor (x / 32)) % mod
        x = (x xor (x * 2048)) % mod
        return x.toInt()
    }

    fun part1(input: String): Long {
        val secrets = input.lines().map { it.toInt() }
        return secrets.sumOf { (1..2000).fold(it) { acc, _ -> nextSecret(acc) }.toLong() }
    }

    fun part2(input: String): Int {
        val secrets = input.lines().map { it.toInt() }
        val pricesByWindow = IntArray(2 shl 20)
        val seen = IntArray(2 shl 20)

        var previousPrice = 0
        for ((seller, secret) in secrets.withIndex()) {
            var windowId = 0
            var value = secret

            repeat(2000) { i ->
                val price = value % 10
                val delta = previousPrice - price
                windowId = ((windowId shl 5) or (delta + 9)) and ((2 shl 20) - 1)

                if (i > 4 && seen[windowId] != seller + 1) {
                    seen[windowId] = seller + 1
                    pricesByWindow[windowId] += price
                }

                value = nextSecret(value)
                previousPrice = price
            }
        }

        return pricesByWindow.max()
    }

    check(part1(readInput("Day22_test")) == 37327623L)
    check(part2(readInput("Day22_test2")) == 23)
    val input = readInput("Day22")

    part1(input).println()
    part2(input).println()
}
