import java.util.ArrayDeque

fun main() {
    fun solve(input: List<String>, startBeam: Beam): Int {
        val (n, m) = findSize(input)
        val visited = Array(n) { Array(m) { BooleanArray(Direction.entries.size) } }
        val beams = ArrayDeque<Beam>()

        fun tryAddNextBeam(i0: Int, j0: Int, d: Direction) {
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

                '|' -> if (d == Direction.LEFT || d == Direction.RIGHT) {
                    tryAddNextBeam(i, j, Direction.UP)
                    tryAddNextBeam(i, j, Direction.DOWN)
                } else {
                    tryAddNextBeam(i, j, d)
                }

                '-' -> if (d == Direction.UP || d == Direction.DOWN) {
                    tryAddNextBeam(i, j, Direction.LEFT)
                    tryAddNextBeam(i, j, Direction.RIGHT)
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
}
