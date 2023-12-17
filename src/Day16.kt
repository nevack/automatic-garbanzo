import java.util.ArrayDeque

fun main() {
    fun solve(input: List<String>, startBeam: Beam): Int {
        val (n, m) = findSize(input)
        val visited = Array(n) { Array(m) { BooleanArray(Direction.entries.size) } }
        val beams = object : ArrayDeque<Beam>() {
            override fun add(element: Beam): Boolean {
                if (element.i !in 0..<n || element.j !in 0..<m) return false
                if (visited[element.i][element.j][element.direction.ordinal]) return false
                visited[element.i][element.j][element.direction.ordinal] = true
                return super.add(element)
            }
        }

        beams += startBeam moveTo startBeam.direction

        while (beams.any()) {
            val beam = beams.removeFirst()
            when (input[beam.i][beam.j]) {
                '\\' -> beams += beam moveTo (beam.direction xor 0b01)

                '/' -> beams += beam moveTo (beam.direction xor 0b11)

                '|' -> if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
                    beams += beam moveTo Direction.UP
                    beams += beam moveTo Direction.DOWN
                } else {
                    beams += beam moveTo beam.direction
                }

                '-' -> if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
                    beams += beam moveTo Direction.LEFT
                    beams += beam moveTo Direction.RIGHT
                } else {
                    beams += beam moveTo beam.direction
                }

                else -> beams += beam moveTo beam.direction
            }
        }

        return visited.sumOf { row ->
            row.count { cell ->
                cell.any { direction -> direction }
            }
        }
    }

    fun part1(input: List<String>): Int {
        return solve(input, Beam(0, -1, Direction.RIGHT))
    }

    fun part2(input: List<String>): Int {
        val (n, m) = findSize(input)
        return listOf(
            (0..<n).maxOf { i -> solve(input, Beam(i, -1, Direction.RIGHT)) },
            (0..<m).maxOf { j -> solve(input, Beam(-1, j, Direction.DOWN)) },
            (0..<n).maxOf { i -> solve(input, Beam(i, m, Direction.LEFT)) },
            (0..<m).maxOf { j -> solve(input, Beam(n, j, Direction.UP)) },
        ).max()
    }

    val testInput1 = readInput("Day16_test1")
    check(part1(testInput1) == 46)
    check(part2(testInput1) == 51)

    val input = readInput("Day16")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}

private data class Beam(
    val i: Int,
    val j: Int,
    val direction: Direction,
) {
    infix fun moveTo(direction: Direction): Beam {
        return Beam(i + direction.di, j + direction.dj, direction)
    }
}
