import kotlin.math.abs

fun main() {
    fun parseLine(line: String): Pair<Direction, Int> {
        val (directionStr, countStr, _) = line.split(" ")
        val direction = when(directionStr) {
            "R" -> Direction.RIGHT
            "D" -> Direction.DOWN
            "L" -> Direction.LEFT
            "U" -> Direction.UP
            else -> error { "Cannot parse direction '$directionStr'" }
        }
        val count = countStr.toInt()

        return direction to count
    }

    fun part1(input: List<String>): Int {
        val moves = input.map { line -> parseLine(line) }

        val exterior = moves.sumOf { (_, count) -> count }
        var interior = 0

        moves.fold(0 to 0) { cur, (direction, count) ->
            val next = cur + (direction * count)
            interior += cur cross next
            next
        }

        return abs(interior) / 2 + exterior / 2 + 1
    }

    val testInput1 = readInput("Day18_test1")
    check(part1(testInput1) == 62)

    val input = readInput("Day18")
    timed("Part1 answer") { part1(input) }
    // timed("Part2 answer") { part2(input) }
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

private operator fun Direction.times(x: Int): Pair<Int, Int> {
    return di * x to dj * x
}

private infix fun Pair<Int, Int>.cross(other: Pair<Int, Int>): Int {
    return (other.second - second) * (other.first + first)
}
