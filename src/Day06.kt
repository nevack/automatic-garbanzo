
fun main() {
    fun parseRaw(input: List<String>): Pair<List<String>, List<String>> {
        check(input.size == 2)
        val times = input[0].substringAfter("Time:     ").split(' ').filter { it.isNotEmpty() }
        val dists = input[1].substringAfter("Distance: ").split(' ').filter { it.isNotEmpty() }
        check(times.size == dists.size)
        return times to dists
    }

    fun parse1(input: List<String>): List<Race> {
        val (times, dists) = parseRaw(input)

        return times.zip(dists) { time, dist -> Race(time.toLong(), dist.toLong()) }
    }

    fun parse2(input: List<String>): Race {
        val (times, dists) = parseRaw(input)
        val time = times.joinToString("").toLong()
        val dist = dists.joinToString("").toLong()

        return Race(time, dist)
    }

    fun solve(race: Race): Int {
        return (1..race.time).filter { time ->
            race.dist < time * (race.time - time)
        }.size
    }

    fun part1(input: List<String>): Int {
        val races = parse1(input)

        return races.fold(1) { cur, race -> cur * solve(race) }
    }

    fun part2(input: List<String>): Int {
        val race = parse2(input)
        return solve(race)
    }

    val testInput1 = readInput("Day06_test1")
    check(part1(testInput1) == 288)
    check(part2(testInput1) == 71503)

    val input = readInput("Day06")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part1 answer")
}

data class Race(
    val time: Long,
    val dist: Long,
)
