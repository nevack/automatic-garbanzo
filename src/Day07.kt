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

    val testInput1 = readInput("Day07_test1")
    check(part1(testInput1) == 6440)

    val input = readInput("Day07")
    part1(input).printlnPrefix("Part1 answer")
}

data class Hand(
    val cards: String,
    val bid: Int,
) {
    fun type(): Int {
        val charMap = mutableMapOf<Char, Int>()
        for (card in cards) {
            charMap[card] = charMap.getOrDefault(card, 0) + 1
        }

        if (charMap.any { (_, v) -> v == 5 }) {
            return 9
        }

        if (charMap.any { (_, v) -> v == 4 }) {
            return 8
        }

        if (charMap.any { (_, v) -> v == 3 }) {
            if (charMap.any { (_, v) -> v == 2 }) {
                return 7
            }
            return 6
        }

        if (charMap.count { (_, v) -> v == 2 } == 2) {
            return 5
        }

        if (charMap.count { (_, v) -> v == 2 } == 1) {
            return 4
        }

        if (charMap.count { (_, v) -> v == 1 } == 5) {
            return 3
        }

        return 0
    }

    fun cardsForSorting(): String {
        return cards.map { card ->
            when (card) {
                'A' -> 'Z'
                'K' -> 'Y'
                'Q' -> 'X'
                'J' -> 'W'
                'T' -> 'V'
                else -> card
            }
        }.joinToString("")
    }
}
