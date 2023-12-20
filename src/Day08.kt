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

    fun part2(input: List<String>): Long {
        val (moves, mapped)  = parse(input)

        return mapped.keys.filter { it.last() == 'A' }.map { start ->
            var index = 0
            var current = start
            val visited = mutableMapOf<Pair<Int, String>, Int>()

            while (true) {
                val adjusted = index % moves.size
                val step = adjusted to current
                if (step in visited) break
                visited[step] = index
                current = checkNotNull(
                    when (moves[adjusted]) {
                        "L" -> mapped[current]?.first
                        "R" -> mapped[current]?.second
                        else -> null
                    }
                )
                index++
            }
            val last = checkNotNull(visited[index % moves.size to current])
            index - last // Loop Size
        }.fold(1L) { a, b -> lcm(a, b.toLong()) }
    }

    val testInput1 = readInput("Day08_test1")
    check(part1(testInput1) == 2)
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput2) == 6)
    val testInput3 = readInput("Day08_test3")
    check(part2(testInput3) == 6L)

    val input = readInput("Day08")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}

val REGEX = """(\w+) = \((\w+), (\w+)\)""".toRegex()
