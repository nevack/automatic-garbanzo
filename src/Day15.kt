fun main() {
    fun parse(input: List<String>): List<String> {
        return input.joinToString("").split(",")
    }

    fun hash(input: String): Int {
        return input.fold(0) { acc, char ->
            ((acc + char.code) * 17) % 256
        }
    }

    fun part1(input: List<String>): Int {
        val parts = parse(input)

        return parts.sumOf { part -> hash(part) }
    }

    val testInput1 = readInput("Day15_test1")
    check(part1(testInput1) == 1320)

    val input = readInput("Day15")
    part1(input).printlnPrefix("Part1 answer")
}
