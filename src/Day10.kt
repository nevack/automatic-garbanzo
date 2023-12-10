fun main() {
    fun findSize(input: List<String>): Pair<Int, Int> {
        val n = input.size
        val m = input.first().length

        input.forEach { line ->
            check(line.length == m)
        }

        return n to m
    }

    fun findStart(input: List<String>): Pair<Int, Int> {
        check(input.count { "S" in it } == 1)
        val i = checkNotNull(input.indexOfFirst { "S" in it })

        check(input[i].count { 'S' == it } == 1)
        val j = input[i].indexOf('S')

        return i to j
    }

    fun part1(input: List<String>): Int {
        val size = findSize(input)
        val start = findStart(input)

        var prev = -1 to -1
        var cur = start
        var availableMoves = listOf(true, true, true, true)

        val loop = ArrayList<Pair<Int, Int>>()
        while (true) {
            loop += cur
            for (move in around.indices) {
                if (!availableMoves[move]) continue

                val next = cur + around[move]
                // Out of bounds
                if (next !in size) continue
                // Skip move we came from
                if (next == prev) continue
                // Loop found
                if (next == start) return loop.size / 2

                val nextAvailableMoves = pipesToMoves[input[next]] ?: continue
                prev = cur
                cur = next
                availableMoves = nextAvailableMoves
                break
            }
        }
    }

    val testInput1 = readInput("Day10_test1")
    check(part1(testInput1) == 8)

    val input = readInput("Day10")
    part1(input).printlnPrefix("Part1 answer")
}

// Pipe to (Right,Bottom,Left,Top) move availability.
val pipesToMoves = mapOf(
    '|' to listOf(false, true, false, true),
    '-' to listOf(true, false, true, false),
    'L' to listOf(true, false, false, true),
    'F' to listOf(true, true, false, false),
    '7' to listOf(false, true, true, false),
    'J' to listOf(false, false, true, true),
)

val around = listOf(
    0 to 1, // Right
    1 to 0, // Bottom
    0 to -1, // Left
    -1 to 0, // Top
)

private operator fun Pair<Int, Int>.contains(other: Pair<Int, Int>): Boolean {
    return other.first in 0..<first && other.second in 0..<second
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

private operator fun List<String>.get(index: Pair<Int, Int>): Char {
    return this[index.first][index.second]
}
