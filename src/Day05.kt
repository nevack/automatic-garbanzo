fun main() {
    fun parseLine(line: String): Int {
        return 0
    }

    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter("seeds: ").split(' ').map { it.toLong() }
        val seed2soil = mutableListOf<Range>()
        val soil2fert = mutableListOf<Range>()
        val fert2water = mutableListOf<Range>()
        val water2light = mutableListOf<Range>()
        val light2temp = mutableListOf<Range>()
        val temp2humid = mutableListOf<Range>()
        val humid2loc = mutableListOf<Range>()

        var idx = 0
        for (line in input) {
            if ("seeds: " in line) continue
            if (line.isEmpty()) continue
            if (" map:" in line) {
                idx++
                continue
            }
            val (dest, source, size) = line.split(' ').map { it.toLong() }
            when (idx) {
                1 -> seed2soil += Range(source, dest, size)
                2 -> soil2fert += Range(source, dest, size)
                3 -> fert2water += Range(source, dest, size)
                4 -> water2light += Range(source, dest, size)
                5 -> light2temp += Range(source, dest, size)
                6 -> temp2humid += Range(source, dest, size)
                7 -> humid2loc += Range(source, dest, size)
            }
        }

        val locs = mutableListOf<Long>()
        for (seed in seeds) {
            var soil = seed
            for (range in seed2soil) {
                if (seed in range.source..(range.source + range.size - 1)) {
                    soil = range.dest - range.source + seed
                    break
                }
            }
            var fert = soil
            for (range in soil2fert) {
                if (soil in range.source..(range.source + range.size - 1)) {
                    fert = range.dest - range.source + soil
                    break
                }
            }
            var water = fert
            for (range in fert2water) {
                if (fert in range.source..(range.source + range.size - 1)) {
                    water = range.dest - range.source + fert
                    break
                }
            }
            var light = water
            for (range in water2light) {
                if (water in range.source..(range.source + range.size - 1)) {
                    light = range.dest - range.source + water
                    break
                }
            }
            var temp = light
            for (range in light2temp) {
                if (light in range.source..(range.source + range.size - 1)) {
                    temp = range.dest - range.source + light
                    break
                }
            }
            var humid = temp
            for (range in temp2humid) {
                if (temp in range.source..(range.source + range.size - 1)) {
                    humid = range.dest - range.source + temp
                    break
                }
            }
            var loc = humid
            for (range in humid2loc) {
                if (humid in range.source..(range.source + range.size - 1)) {
                    loc = range.dest - range.source + humid
                    break
                }
            }
            locs += loc
        }

        return locs.min()
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
