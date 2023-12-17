fun main() {
    fun part1(input: List<String>): Int {
        return GridScanner(input).process().first
    }

    fun part2(input: List<String>): Int {
        return GridScanner(input).process().second
    }

    val testInput1 = readInput("Day03_test1")
    check(part1(testInput1) == 4361)
    check(part2(testInput1) == 467835)

    val input = readInput("Day03")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}

class GridScanner(
    input: List<String>,
) {
    private val rowSize: Int

    private val scannerLines: Sequence<String>

    init {
        val firstRowSize = input.first().length
        val lastRowSize = input.last().length
        require(firstRowSize == lastRowSize)
        rowSize = firstRowSize + 2

        val firstRow = ".".repeat(rowSize)
        val lastRow = ".".repeat(rowSize)

        scannerLines = sequenceOf(firstRow) + input.asSequence().map { ".$it." } + sequenceOf(lastRow)
    }

    fun process(): Pair<Int, Int> {
        val gears = mutableMapOf<Pair<Int, Int>, MutableSet<Int>>()

        val scanWindow = scannerLines.windowed(size = SIZE)
        val sum = scanWindow.mapIndexed { windowIndex, rows ->
            rows[1].numbers().sumOf { (number, range) ->
                range.find { index ->
                    around().any { (i, j) ->
                        val scanned = rows[i][j + index - 1]
                        if (scanned == '*') {
                            gears.getOrPut((i + windowIndex) to (j + index - 1)) {
                                mutableSetOf()
                            }.add(number)
                        }
                        !scanned.isDigit() && scanned != '.'
                    }
                }?.let{ number } ?: 0
            }
        }.sum()

        val power: Int = gears.values.filter { gear -> gear.size >= 2 }.sumOf { numbers ->
            numbers.fold(1) { a: Int, b: Int -> a * b }
        }

        return sum to power
    }

    private fun String.numbers(): Sequence<Pair<Int, IntRange>> {
        return REGEX.findAll(this).map { match -> match.value.toInt() to match.range }
    }

    private fun around(): Sequence<Pair<Int, Int>> = sequence {
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                yield(i to j)
            }
        }
    }

    companion object {
        private val REGEX = """(\d+)""".toRegex()
        private const val SIZE = 3
    }
}
