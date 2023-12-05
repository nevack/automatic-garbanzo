fun main() {
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
        val seeds = input[0].substringAfter("seeds: ").split(' ').map { it.toLong() }

        val mapper = mutableListOf<MutableList<Range>>()

        for (line in input) {
            if ("seeds: " in line) continue
            if (line.isEmpty()) continue
            if (" map:" in line) {
                mapper += mutableListOf<Range>()
                continue
            }
            val (dest, source, size) = line.split(' ').map { it.toLong() }
            mapper.last() += Range(source, dest, size)
        }

        return seeds.map { solve(it, mapper) }.min()
    }

    val testInput1 = readInput("Day05_test1")
    check(part1(testInput1) == 35L)

    val input = readInput("Day05")
    part1(input).printlnPrefix("Part1 answer")
}

data class Range(
    val source: Long,
    val dest: Long,
    val size: Long,
)
