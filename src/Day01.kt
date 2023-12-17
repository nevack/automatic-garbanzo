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

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val left = line.findAnyOf(scanMap.keys)!!.second.scanMap()
            val right = line.findLastAnyOf(scanMap.keys)!!.second.scanMap()
            sum += left * 10 + right
        }
        return sum
    }

    val testInput1 = readInput("Day01_test1")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}

private val scanMap = mapOf(
    "zero" to 0,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
    "0" to 0,
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
)

private fun String.scanMap(): Int = scanMap[this]!!
