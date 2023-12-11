fun main() {
    fun parse(input: List<String>): Array<CharArray> {
        return Array(input.size) { index -> input[index].toCharArray() }
    }

    fun findSize(input: List<String>): Pair<Int, Int> {
        val n = input.size
        val m = input.first().length

        input.forEach { line -> check(line.length == m) }

        return n to m
    }

    fun part1(input: List<String>): Int {
        val field = parse(input)
        val (n, m) = findSize(input)

        val horizontalFactors = IntArray(m) { i ->
            if ((0..<m).all { j -> field[i][j] == '.'}) 2 else 1
        }
        val verticalFactors = IntArray(n) { j ->
            if ((0..<n).all { i -> field[i][j] == '.'}) 2 else 1
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

        val sumHorizontal = horizontalPaths.zip(horizontalFactors).sumOf { (paths, factor) -> paths * factor }
        val sumVertical = verticalPaths.zip(verticalFactors).sumOf { (paths, factor) -> paths * factor }

        return sumHorizontal + sumVertical
    }

    val testInput1 = readInput("Day11_test1")
    check(part1(testInput1) == 374)

    val input = readInput("Day11")
    part1(input).printlnPrefix("Part1 answer")
}
