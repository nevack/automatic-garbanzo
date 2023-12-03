fun main() {
    fun part1(input: List<String>): Int {
        return GridScanner(input).process().first
    }

    fun part2(input: List<String>): Int {
        return GridScanner(input).process().second
    }

    val testInput1 = readInput("Day03_test1")
    check(part1(testInput1) == 4361)
    val testInput2 = readInput("Day03_test1")
    check(part2(testInput2) == 467835)

    val input = readInput("Day03")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
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

        var sum = 0
        val scanWindow = scannerLines.windowed(size = SIZE)
        scanWindow.forEachIndexed { windowIndex, rows ->
            val numbers = rows[1].numbers()

            for ((number, range) in numbers) {
                var found = false
                range.forEach { index ->
                    for (i in 0 until SIZE) {
                        for (j in 0 until SIZE) {
                            val scanned = rows[i][j + index - 1]
                            if (!scanned.isDigit() && scanned != '.') {
                                found = true
                            }
                            if (scanned == '*') {
                                gears.getOrPut((i + windowIndex) to (j + index - 1)) {
                                    mutableSetOf()
                                }.add(number)
                            }
                        }
                    }
                }
                if (found) {
                    sum += number
                }
            }
        }

        val power: Int = gears.values.filter { gear -> gear.size >= 2 }.sumOf { numbers ->
            numbers.fold(1) { a: Int, b: Int -> a * b }
        }

        return sum to power
    }

    private fun String.numbers(): Sequence<Pair<Int, IntRange>> {
        return REGEX.findAll(this).map { match -> match.value.toInt() to match.range }
    }

    companion object {
        private val REGEX = """(\d+)""".toRegex()
        private const val SIZE = 3
    }
}
