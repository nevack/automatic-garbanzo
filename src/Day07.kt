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
    val testInput2 = readInput("Day07_test1")
    check(part2(testInput2) == 5905)

    val input = readInput("Day07")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}

data class Hand(
    val cards: String,
    val bid: Int,
) {
    fun type(withJoker: Boolean = false): Int {
        val charMap = cards.groupingBy { it }.eachCount()
        val jokers = if (withJoker) cards.count { card -> card == 'J'} else 0

        if (charMap.any { (_, v) -> v == 5 }) {
            return 9 // Five
        }

        if (charMap.any { (_, v) -> v == 4 }) {
            if (jokers == 1 || jokers == 4) {
                return 9 // Five
            }
            return 8 // Four
        }

        if (charMap.any { (_, v) -> v == 3 }) {
            if (jokers in 1..2) {
                return 7 + jokers // Four or Five
            }
            if (charMap.any { (_, v) -> v == 2 }) {
                if (jokers == 3) {
                    return 9 // Five
                }
                return 7 // Full House
            }
            return 6 // Three
        }

        if (charMap.count { (_, v) -> v == 2 } == 2) {
            if (jokers in 1..2) {
                return 6 + jokers // Full House or Four
            }
            return 5 // Two Pair
        }

        if (charMap.count { (_, v) -> v == 2 } == 1) {
            if (jokers in 1..2) {
                return 6 // Three
            }
            return 4 // One Pair
        }

        if (charMap.count { (_, v) -> v == 1 } == 5) {
            if (jokers == 1) {
                return 4 // One Pair
            }
            return 3 // High card
        }

        return 0 // Nothing
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
