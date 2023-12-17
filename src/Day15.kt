fun main() {
    fun parse(input: List<String>): List<String> {
        return input.joinToString("").split(",")
    }

    fun hash(input: String): Int {
        return input.fold(0) { acc, char ->
            ((acc + char.code) * 17) % 256
        }
    }

    fun part1(input: List<String>): Int {
        val parts = parse(input)

        return parts.sumOf { part -> hash(part) }
    }

    fun part2(input: List<String>): Int {
        val parts = parse(input)

        val boxes = mutableMapOf<Int, MutableMap<String, Int>>()

        for (part in parts) {
            if (part.endsWith("-")) {
                val label = part.removeSuffix("-")
                boxes[hash(label)]?.remove(label)
            } else {
                val (label, numberStr) = part.split("=")
                boxes.getOrPut(hash(label)) { mutableMapOf() }.put(label, numberStr.toInt())
            }
        }

        return boxes.entries.sumOf { (box, lenses) ->
            lenses.values.withIndex().sumOf { (lensIndex, focalLength) ->
                (box + 1) * (lensIndex + 1) * focalLength
            }
        }
    }

    val testInput1 = readInput("Day15_test1")
    check(part1(testInput1) == 1320)
    check(part2(testInput1) == 145)

    val input = readInput("Day15")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}
