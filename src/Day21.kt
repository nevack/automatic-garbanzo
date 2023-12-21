import java.math.BigInteger

fun main() {
    fun findStart(input: List<String>): Pair<Int, Int> {
        val i = input.indexOfFirst { line -> 'S' in line }
        val j = input[i].indexOfFirst { char -> char == 'S' }
        return i to j
    }

    fun part1(input: List<String>, stepCount: Int): Int {
        val size = findSize(input)
        val start = findStart(input)

        return (1..stepCount).fold(listOf(start)) { current, _ ->
            current.flatMap { cell ->
                Direction.entries.map { direction -> cell moveTo direction }
            }.filter { cell ->
                cell in size
            }.filter { (i, j) ->
                input[i][j] != '#'
            }.distinct()
        }.size
    }

    fun solveQuadratic(a0: BigInteger, a1: BigInteger, a2: BigInteger, n: BigInteger): BigInteger {
        val b1 = a1 - a0
        val b2 = a2 - a1
        return a0 + b1 * n + (n * (n - 1.toBigInteger()) / 2.toBigInteger()) * (b2 - b1)
    }

    fun part2(input: List<String>, stepCount: Int): BigInteger {
        val (n, m) = findSize(input)
        val start = findStart(input)
        check(n == m)

        var current = setOf(start)
        val res = mutableListOf<Int>()

        for (step in 1..stepCount) {
            val next = hashSetOf<Pair<Int, Int>>()

            current.flatMapTo(next) { cell ->
                Direction.entries.map { direction -> cell moveTo direction }.filter { (i, j) ->
                    input[i.mod(n)][j.mod(m)] != '#'
                }
            }

            current = next

            if (step % n == stepCount % n) {
                res += current.size
            }

            if (res.size >= 3) {
                break
            }
        }

        val (a0, a1, a2) = res.map { it.toBigInteger() }

        return solveQuadratic(a0, a1, a2, (stepCount / n).toBigInteger())
    }

    val testInput = readInput("Day21_test1")
    check(part1(testInput, 6) == 16)

    val input = readInput("Day21")
    timed("Part1 answer") { part1(input, 64) }
    timed("Part2 answer") { part2(input, 26501365) }
}
