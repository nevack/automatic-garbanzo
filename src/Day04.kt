fun main() {
    fun parseLine(line: String): Int {
        val numbersStr = line.substringAfter(": ")
        val (winningStr, haveStr) = numbersStr.split(" | ")
        val winning = winningStr.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
        val have = haveStr.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }

        return have.count { it in winning }
    }

    fun part1(input: List<String>): Int {
        return input.map { parseLine(it) }.filter { it > 0 }.sumOf { matches -> 1 shl (matches - 1) }
    }

    fun part2(input: List<String>): Int {
        val counts = IntArray(input.size) { 1 }

        return input.map { parseLine(it) }.withIndex().sumOf { (i, matches) ->
            for (next in 1..matches) {
                counts[i + next] += counts[i]
            }
            counts[i]
        }
    }

    val testInput1 = readInput("Day04_test1")
    check(part1(testInput1) == 13)
    check(part2(testInput1) == 30)

    val input = readInput("Day04")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}
