fun main() {
    fun parseLine(line: String): List<Int> {
        return line.split(" ").map { it.toInt() }
    }

    fun solve(progression: List<Int>): Int {
        val last = mutableListOf<Int>()
        var level = progression

        while (!level.all { it == 0 }) {
            last += level.last()
            level = level.windowed(2) { it[1] - it[0] }
        }

        return last.sum()
    }

    fun part1(input: List<String>): Int {
        return input.map { line -> parseLine(line) }.sumOf { progression -> solve(progression) }
    }

    fun part2(input: List<String>): Int {
        return input.map { line -> parseLine(line).reversed() }.sumOf { progression -> solve(progression) }
    }

    val testInput1 = readInput("Day09_test1")
    check(part1(testInput1) == 114)
    check(part2(testInput1) == 2)

    val input = readInput("Day09")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}
