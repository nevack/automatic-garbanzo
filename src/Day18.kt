import kotlin.math.abs

fun main() {
    fun parseLine1(line: String): Pair<Direction, Int> {
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

    fun parseLine2(line: String): Pair<Direction, Int> {
        val hexStr = line.split(" ").last().removeSurrounding("(#", ")")
        val direction = Direction.entries[hexStr.last().digitToInt()]
        val count = hexStr.dropLast(1).toInt(16)

        return direction to count
    }

    fun solve(moves: List<Pair<Direction, Int>>): Long {
        val exterior = moves.sumOf { (_, count) -> count.toLong() }
        var interior = 0L

        moves.fold(0 to 0) { cur, (direction, count) ->
            val next = cur + (direction * count)
            interior += (next.second - cur.second).toLong() * (next.first + next.first)
            next
        }

        return abs(interior) / 2 + exterior / 2 + 1
    }

    fun part1(input: List<String>): Long {
        val moves = input.map { line -> parseLine1(line) }

        return solve(moves)
    }

    fun part2(input: List<String>): Long {
        val moves = input.map { line -> parseLine2(line) }

        return solve(moves)
    }

    val testInput1 = readInput("Day18_test1")
    check(part1(testInput1) == 62L)
    check(part2(testInput1) == 952408144115L)

    val input = readInput("Day18")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

private operator fun Direction.times(x: Int): Pair<Int, Int> {
    return di * x to dj * x
}
