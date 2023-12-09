fun main() {
    fun parseLine(line: String): List<Int> {
        return line.split(" ").map { it.toInt() }
    }

    fun part1(input: List<String>): Int {
        return input.map { line -> parseLine(line) }.sumOf { progression ->
            val last = mutableListOf<Int>()
            var level = progression

            while (!level.all { it == 0 }) {
                last += level.last()
                level = level.windowed(2) { it[1] - it[0] }
            }

            last.sum()
        }
    }

    val testInput1 = readInput("Day09_test1")
    check(part1(testInput1) == 114)

    val input = readInput("Day09")
    part1(input).printlnPrefix("Part1 answer")
}
