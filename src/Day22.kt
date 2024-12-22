fun main() {

    fun nextSecret(x: Int): Int {
        val mod = 16777216L

        var x = x.toLong()
        x = (x xor (x * 64)) % mod
        x = (x xor (x / 32)) % mod
        x = (x xor (x * 2048)) % mod
        return x.toInt()
    }

    fun windowId(prices: List<Int>): Int {
        val ds = prices.deltas()
        return ds[0] * 10000000 + ds[1] * 10000 + ds[2] * 100 + ds[3]
    }

    fun part1(input: String): Long {
        val secrets = input.lines().map { it.toInt() }
        return secrets.sumOf { (1..2000).fold(it) { acc, _ -> nextSecret(acc) }.toLong() }
    }

    fun part2(input: String): Int {
        val secrets = input.lines().map { it.toInt() }

        val sellerPricesByWindow = secrets.map { secret ->
            val prices = generateSequence(secret) { nextSecret(it) }.map { it % 10 }.take(2000).toList()
            prices.windowed(5).reversed().associate { windowId(it) to it.last() }
        }

        val candidateWindows = sellerPricesByWindow.flatMap { it.keys }.toSet()
        return candidateWindows.maxOf { window -> sellerPricesByWindow.sumOf { it[window] ?: 0 } }
    }

    check(part1(readInput("Day22_test")) == 37327623L)
    check(part2(readInput("Day22_test2")) == 23)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
