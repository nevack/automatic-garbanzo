fun main() {
    fun parse(input: List<String>): Pair<List<Long>, List<List<Range>>> {
        val seeds = input[0].substringAfter("seeds: ").split(' ').map { it.toLong() }

        val mappers = mutableListOf<MutableList<Range>>()

        for (line in input) {
            if ("seeds: " in line) continue
            if (line.isEmpty()) continue
            if (" map:" in line) {
                mappers += mutableListOf<Range>()
                continue
            }
            val (dest, source, size) = line.split(' ').map { it.toLong() }
            mappers.last() += Range(source, dest, size)
        }

        return seeds to mappers
    }

    fun solve(seed: Long, mappers: List<List<Range>>): Long {
        var res = seed
        for (mapper in mappers) {
            for (range in mapper) {
                if (res in range.source..<range.source + range.size) {
                    res += range.dest - range.source
                    break
                }
            }
        }
        return res
    }

    fun part1(input: List<String>): Long {
        val (seeds, mappers) = parse(input)

        return seeds.map { seed -> solve(seed, mappers) }.min()
    }

    fun part2(input: List<String>): Long {
        val (seeds, mappers) = parse(input)
        val seedRanges = seeds.windowed(2, 2)
        var min = Long.MAX_VALUE
        for ((start, count) in seedRanges) {
            for (i in 0 until count) {
                val solved = solve(start + i, mappers)
                min = kotlin.math.min(min, solved)
            }
        }
        return min
    }

    val testInput1 = readInput("Day05_test1")
    check(part1(testInput1) == 35L)

    val testInput2 = readInput("Day05_test1")
    check(part2(testInput2) == 46L)

    val input = readInput("Day05")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}

data class Range(
    val source: Long,
    val dest: Long,
    val size: Long,
)
