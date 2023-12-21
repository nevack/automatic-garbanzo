fun main() {
    fun findStart(input: List<String>): Pair<Int, Int> {
        val i = input.indexOfFirst { line -> 'S' in line }
        val j = input[i].indexOfFirst { char -> char == 'S' }
        return i to j
    }

    fun part1(input: List<String>, stepCount: Int): Int {
        val size = findSize(input)
        val start = findStart(input)

        return (1..stepCount).fold(listOf(start)) { current, _ ->
            current.flatMap { cell ->
                Direction.entries.map { direction -> cell moveTo direction }
            }.filter { cell ->
                cell in size
            }.filter { (i, j) ->
                input[i][j] != '#'
            }.distinct()
        }.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day21_test1")
    check(part1(testInput, 6) == 16)
    check(part2(testInput) == 11)

    val input = readInput("Day21")
    timed("Part1 answer") { part1(input, 64) }
    timed("Part2 answer") { part2(input) }
}
