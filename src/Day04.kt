import kotlin.math.pow

fun main() {
    fun parseLine(line: String): Card {
        val (_, numbersStr) = line.split(": ")
        val (winningStr, haveStr) = numbersStr.split(" | ")
        val winning = winningStr.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
        val have = haveStr.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }

        return Card(winning.toSet(), have.toSet())
    }

    fun part1(input: List<String>): Int {
        return input.map { parseLine(it) }.sumOf { card ->
            val count = card.have.intersect(card.winning).size
            2.0.pow(count - 1).toInt()
        }
    }

    val testInput1 = readInput("Day04_test1")
    check(part1(testInput1) == 13)

    val input = readInput("Day04")
    part1(input).printlnPrefix("Part1 answer")
}

class Card(
    val winning: Set<Int>,
    val have: Set<Int>,
)
