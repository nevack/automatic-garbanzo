fun main() {
    fun parse(input: List<String>): Array<CharArray> {
        return Array(input.size) { index -> input[index].toCharArray() }
    }

    fun solve(input: List<String>, factor: Int = 2): Long {
        val field = parse(input)
        val (n, m) = findSize(input)

        val horizontalFactors = IntArray(m) { i ->
            if ((0..<m).all { j -> field[i][j] == '.'}) factor else 1
        }
        val verticalFactors = IntArray(n) { j ->
            if ((0..<n).all { i -> field[i][j] == '.'}) factor else 1
        }

        val galaxies = mutableListOf<Pair<Int, Int>>()

        field.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char == '#') galaxies += i to j
            }
        }

        val verticalPaths = IntArray(n)
        val horizontalPaths = IntArray(m)

        fun IntArray.inc(a: Int, b: Int) {
            for (i in minOf(a, b)..<maxOf(a, b)) this[i]++
        }

        for (first in galaxies.indices) {
            for (second in first + 1..<galaxies.size) {
                horizontalPaths.inc(galaxies[first].first, galaxies[second].first)
                verticalPaths.inc(galaxies[first].second, galaxies[second].second)
            }
        }

        val sumHorizontal = horizontalPaths.zip(horizontalFactors).sumOf { (paths, factor) -> 1L * paths * factor }
        val sumVertical = verticalPaths.zip(verticalFactors).sumOf { (paths, factor) -> 1L * paths * factor }

        return sumHorizontal + sumVertical
    }

    fun part1(input: List<String>): Long {
        return solve(input)
    }

    fun part2(input: List<String>, factor: Int = 1_000_000): Long {
        return solve(input, factor)
    }

    val testInput1 = readInput("Day11_test1")
    check(part1(testInput1) == 374L)
    check(part2(testInput1, 10) == 1030L)
    check(part2(testInput1, 100) == 8410L)

    val input = readInput("Day11")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}
