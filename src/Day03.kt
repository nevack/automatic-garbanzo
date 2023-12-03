fun main() {
    fun part1(input: List<String>): Int {
        return GridScanner(input).process()
    }

    val testInput1 = readInput("Day03_test1")
    check(part1(testInput1) == 4361)

    val input = readInput("Day03")
    part1(input).printlnPrefix("Part1 answer")
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

    fun process(): Int {
        var sum = 0
        val scanWindow = scannerLines.windowed(size = SIZE)
        scanWindow.forEach { rows ->
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
                        }
                    }
                }
                if (found) {
                    sum += number
                }
            }
        }

        return sum
    }

    private fun String.numbers(): Sequence<Pair<Int, IntRange>> {
        return REGEX.findAll(this).map { match -> match.value.toInt() to match.range }
    }

    companion object {
        private val REGEX = """(\d+)""".toRegex()
        private const val SIZE = 3
    }
}
