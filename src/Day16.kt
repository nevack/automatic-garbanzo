import java.util.ArrayDeque

fun main() {
    fun solve(input: List<String>, startBeam: Beam): Int {
        val (n, m) = findSize(input)
        val visited = Array(n) { Array(m) { BooleanArray(Beam.Direction.entries.size) } }
        val beams = ArrayDeque<Beam>()

        fun tryAddNextBeam(i0: Int, j0: Int, d: Beam.Direction) {
            val i = i0 + d.di
            val j = j0 + d.dj
            if (i !in 0..<n || j !in 0..<m) return
            if (visited[i][j][d.ordinal]) return
            visited[i][j][d.ordinal] = true
            beams += Beam(i, j, d)
        }

        tryAddNextBeam(startBeam.i, startBeam.j, startBeam.direction)

        while (beams.any()) {
            val (i, j, d) = beams.removeFirst()
            when (input[i][j]) {
                '\\' -> tryAddNextBeam(i, j, d xor 0b01)

                '/' -> tryAddNextBeam(i, j, d xor 0b11)

                '|' -> if (d == Beam.Direction.LEFT || d == Beam.Direction.RIGHT) {
                    tryAddNextBeam(i, j, Beam.Direction.UP)
                    tryAddNextBeam(i, j, Beam.Direction.DOWN)
                } else {
                    tryAddNextBeam(i, j, d)
                }

                '-' -> if (d == Beam.Direction.UP || d == Beam.Direction.DOWN) {
                    tryAddNextBeam(i, j, Beam.Direction.LEFT)
                    tryAddNextBeam(i, j, Beam.Direction.RIGHT)
                } else {
                    tryAddNextBeam(i, j, d)
                }

                else -> tryAddNextBeam(i, j, d)
            }
        }

        return visited.sumOf { row ->
            row.count { cell ->
                cell.any { direction -> direction }
            }
        }
    }

    fun part1(input: List<String>): Int {
        return solve(input, Beam(0, -1, Beam.Direction.RIGHT))
    }

    fun part2(input: List<String>): Int {
        val (n, m) = findSize(input)
        return listOf(
            (0..<n).maxOf { i -> solve(input, Beam(i, -1, Beam.Direction.RIGHT)) },
            (0..<m).maxOf { j -> solve(input, Beam(-1, j, Beam.Direction.DOWN)) },
            (0..<n).maxOf { i -> solve(input, Beam(i, m, Beam.Direction.LEFT)) },
            (0..<m).maxOf { j -> solve(input, Beam(n, j, Beam.Direction.UP)) },
        ).max()
    }

    val testInput1 = readInput("Day16_test1")
    check(part1(testInput1) == 46)
    val testInput2 = readInput("Day16_test1")
    check(part2(testInput2) == 51)

    val input = readInput("Day16")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}

data class Beam(
    val i: Int,
    val j: Int,
    val direction: Direction,
) {
    enum class Direction(val di: Int, val dj: Int) {
        RIGHT(0, 1), // 0
        DOWN(1, 0),  // 1
        LEFT(0, -1), // 2
        UP(-1, 0);   // 3

        infix fun xor(x: Int): Direction {
            return Direction.entries[ordinal xor x]
        }
    }
}
