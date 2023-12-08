fun main() {
    fun parse(input: List<String>): Pair<List<String>, Map<String, Pair<String, String>>> {
        val moves = input.first().split("").filter { it.isNotEmpty() }

        val mapped = input.drop(2).associate { line ->
            val match = checkNotNull(REGEX.matchEntire(line))
            val (_, from, left, right) = match.groupValues
            from to (left to right)
        }

        return moves to mapped
    }

    fun part1(input: List<String>): Int {
        val (moves, mapped)  = parse(input)

        val infiniteMoves = sequence { while (true) yieldAll(moves) }

        var cur = "AAA"
        var count = 0
        for (move in infiniteMoves) {
            val next = checkNotNull(mapped[cur])
            when (move) {
                "L" -> cur = next.first
                "R" -> cur = next.second
            }
            count++
            if (cur == "ZZZ") {
                break
            }
        }

        return count
    }

    val testInput1 = readInput("Day08_test1")
    check(part1(testInput1) == 2)
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput2) == 6)

    val input = readInput("Day08")
    part1(input).printlnPrefix("Part1 answer")
}

val REGEX = """(\w+) = \((\w+), (\w+)\)""".toRegex()
