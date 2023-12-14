fun main() {
    fun parse(input: List<String>): Array<CharArray> {
        return Array(input.size) { i -> input[i].toCharArray() }
    }

    fun part1(input: List<String>): Int {
        val a = parse(input)
        val (n, m) = findSize(input)

        for (i in 0..<n) {
            for (j in 0..<m) {
                if (a[i][j] == 'O') {
                    val place = (i downTo 1).firstOrNull { up ->
                        a[up - 1][j] != '.'
                    } ?: 0
                    a[i][j] = '.'
                    a[place][j] = 'O'
                }
            }
        }

        return (n downTo 1).sumOf { row ->
            row * a[n - row].count { it == 'O' }
        }
    }

    val testInput1 = readInput("Day14_test1")
    check(part1(testInput1) == 136)

    val input = readInput("Day14")
    part1(input).printlnPrefix("Part1 answer")
}
