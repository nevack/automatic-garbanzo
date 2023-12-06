
fun main() {
    fun parse(input: List<String>): List<Race> {
        check(input.size == 2)
        val times = input[0].substringAfter("Time:     ").split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        val dists = input[1].substringAfter("Distance: ").split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        check(times.size == dists.size)

        val races = times.mapIndexed { index, time ->
            val dist = dists[index]
            Race(time, dist)
        }

        return races
    }

    fun parse2(input: List<String>): Race {
        check(input.size == 2)
        val time = input[0].substringAfter("Time:     ").split(' ').filter { it.isNotEmpty() }.joinToString("").toLong()
        val dist = input[1].substringAfter("Distance: ").split(' ').filter { it.isNotEmpty() }.joinToString("").toLong()

        return Race(time, dist)
    }

    fun part1(input: List<String>): Int {
        val races = parse(input)

        return races.fold(1) { cur, race ->
            cur * (1..race.time).filter { time ->
                race.dist < time * (race.time - time)
            }.size
        }
    }

    fun part2(input: List<String>): Int {
        val race = parse2(input)

        return (1..race.time).filter { time ->
            race.dist < time * (race.time - time)
        }.size
    }

    val testInput1 = readInput("Day06_test1")
    check(part1(testInput1) == 288)
    val testInput2 = readInput("Day06_test1")
    check(part2(testInput2) == 71503)

    val input = readInput("Day06")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part1 answer")
}

data class Race(
    val time: Long,
    val dist: Long,
)
