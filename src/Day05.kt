import kotlin.math.max
import kotlin.math.min

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

    fun solveRangeIter(scanRanges: List<Pair<Long, Long>>, mapper: List<Range>): List<Pair<Long, Long>> {
        val inside = mutableListOf<Pair<Long, Long>>()
        val outside = mapper.fold(scanRanges) { pending, range ->
            val sourceEnd = range.source + range.size
            val diff = range.dest - range.source
            pending.flatMap { (start, end) ->
                val left = start to (min(end, range.source))
                val center = max(start, range.source) to min(sourceEnd, end)
                val right = max(sourceEnd, start) to end

                val outsides = listOfNotNull(
                    left.takeIf { it.second > it.first },
                    right.takeIf { it.second > it.first }
                )
                if (center.second > center.first) {
                    inside += (center.first + diff) to (center.second + diff)
                }
                outsides
            }
        }
        return inside + outside
    }

    fun solveRange(seedRanges: List<Pair<Long, Long>>, mappers: List<List<Range>>): Long {
        return seedRanges.minOf { seedRange ->
            mappers.fold(listOf(seedRange.first to seedRange.first + seedRange.second)) { scan, mapper ->
                solveRangeIter(scan, mapper)
            }.minOf { it.first }
        }
    }

    fun part1(input: List<String>): Long {
        val (seeds, mappers) = parse(input)

        return seeds.map { seed -> solve(seed, mappers) }.min()
    }

    fun part2(input: List<String>): Long {
        val (seeds, mappers) = parse(input)
        val sortedMappers = mappers.map { mapper -> mapper.sortedBy { range ->  range.source } }
        val seedRanges = seeds.chunked(2) { it[0] to it[1] }

        return solveRange(seedRanges, sortedMappers)
    }

    val testInput1 = readInput("Day05_test1")
    check(part1(testInput1) == 35L)
    check(part2(testInput1) == 46L)

    val input = readInput("Day05")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}

data class Range(
    val source: Long,
    val dest: Long,
    val size: Long,
)
