import kotlin.math.max

fun main() {
    fun parseSet(input: String): Bag {
        val bags = input.split(", ")
        var red = 0
        var green = 0
        var blue = 0
        for (part in bags) {
            val (countStr, color) = part.split(' ')
            val count = countStr.toInt()
            when (color) {
                "red" -> red += count
                "green" -> green += count
                "blue" -> blue += count
            }
        }
        return Bag(red, green, blue)
    }

    fun parseLine(line: String): Game {
        val (gameStr, bagsStr) = line.split(": ")
        val (_, gameIdStr) = gameStr.split(" ")
        val gameId = gameIdStr.toInt()
        val bags = bagsStr.split("; ")
        val cubeSets = bags.map { parseSet(it) }
        return Game(gameId, cubeSets)
    }

    fun parseInput(lines: List<String>): List<Game> {
        return lines.map { parseLine(it) }
    }

    fun part1(input: List<String>): Int {
        val games = parseInput(input)

        val validates = { bag: Bag ->
            bag.red <= 12 && bag.green <= 13 && bag.blue <= 14
        }

        val sum = games.filter { game -> game.bags.all(validates) }.sumOf { it.id }

        return sum
    }

    fun part2(input: List<String>): Int {
        val games = parseInput(input)

        val sum = games.sumOf { game ->
            game.bags.fold(Bag.EMPTY) { a, b ->
                Bag(max(a.red, b.red), max(a.green, b.green), max(a.blue, b.blue))
            }.run { red * green * blue }
        }

        return sum
    }

    val testInput1 = readInput("Day02_test1")
    check(part1(testInput1) == 8)

    val testInput2 = readInput("Day02_test2")
    check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}

data class Bag(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    companion object { val EMPTY = Bag(0, 0, 0) }
}

data class Game(
    val id: Int,
    val bags: List<Bag>,
)
