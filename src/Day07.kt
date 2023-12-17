fun main() {
    fun parse(input: List<String>): List<Hand> {
        return input.map { line ->
            val (cards, bidStr) = line.split(' ')
            check(cards.length == 5)
            val bid = bidStr.toInt()
            Hand(cards, bid)
        }
    }

    fun part1(input: List<String>): Int {
        val hands = parse(input)

        val sorted = hands.sortedBy { hand ->
            hand.type().toString() + hand.cardsForSorting()
        }

        val sum = sorted.withIndex().sumOf { (index, hand) ->
            hand.bid * (index + 1)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val hands = parse(input)

        val sorted = hands.sortedBy { hand ->
            hand.type(withJoker = true).toString() + hand.cardsForSorting(withJoker = true)
        }

        val sum = sorted.withIndex().sumOf { (index, hand) ->
            1 * hand.bid * (index + 1)
        }

        return sum
    }

    val testInput1 = readInput("Day07_test1")
    check(part1(testInput1) == 6440)
    check(part2(testInput1) == 5905)

    val input = readInput("Day07")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }

}

data class Hand(
    val cards: String,
    val bid: Int,
) {
    fun type(withJoker: Boolean = false): Int {
        val jokers = if (withJoker) cards.count { card -> card == 'J'} else 0

        val charMap = cards.groupingBy { it }.eachCount().filterNot {
            withJoker && it.key == 'J' && it.value != 5
        }.toList().sortedByDescending { it.second }.map { it.second }.toIntArray()

        charMap[0] += if (jokers == 5) 0 else jokers

        return 9 - possibleSolutions.indexOfFirst { solution ->
            charMap contentEquals solution
        }
    }

    fun cardsForSorting(withJoker: Boolean = false): String {
        return cards.map { card ->
            when (card) {
                'A' -> 'Z'
                'K' -> 'Y'
                'Q' -> 'X'
                'J' -> if (withJoker) '0' else 'W'
                'T' -> 'V'
                else -> card
            }
        }.joinToString("")
    }
}

val possibleSolutions = listOf(
    intArrayOf(5), // Five
    intArrayOf(4, 1), // Four
    intArrayOf(3, 2), // Full House
    intArrayOf(3, 1, 1), // Three
    intArrayOf(2, 2, 1), // Two Pair
    intArrayOf(2, 1, 1, 1), // One Pair
    intArrayOf(1, 1, 1, 1, 1), // High Card
)
