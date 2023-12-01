fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val left = line.first { it.isDigit() }.digitToInt()
            val right = line.last { it.isDigit() }.digitToInt()
            sum += left * 10 + right
        }
        return sum
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
}
