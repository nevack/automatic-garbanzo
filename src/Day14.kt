fun main() {
    fun parse(input: List<String>): Array<CharArray> {
        return Array(input.size) { i -> input[i].toCharArray() }
    }

    fun rotateToNew(a: Array<CharArray>): Array<CharArray> {
        val n = a.size
        val m = a[0].size
        val b = Array(n) { CharArray(m) { '?' } }
        for (i in 0..<n) {
            for (j in 0..<m) {
                b[j][n - 1 - i] = a[i][j]
            }
        }
        return b
    }

    fun slideInPlace(a: Array<CharArray>): Array<CharArray> {
        val n = a.size
        val m = a[0].size

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

        return a
    }

    fun cycleSlideRotate(a: Array<CharArray>): Array<CharArray> {
        var b = a
        for (i in 1..4) {
            slideInPlace(b)
            b = rotateToNew(b)
        }
        return b
    }

    fun count(a: Array<CharArray>): Int {
        val n = a.size
        return (n downTo 1).sumOf { row ->
            row * a[n - row].count { it == 'O' }
        }
    }

    fun part1(input: List<String>): Int {
        val a = parse(input)
        slideInPlace(a)
        return count(a)
    }

    fun part2(input: List<String>, cycles: Int = 1_000_000_000): Int {
        val a = parse(input)
        val (n, m) = findSize(input)
        check(n == m) { "Must be square!" }

        var current = a
        val checkpoints = mutableMapOf<Int, Int>()
        var cycle = 0

        while (cycle < cycles) {
            val checkpoint = current.contentDeepHashCode()
            val prev = checkpoints.put(checkpoint, cycle)
            if (prev != null) {
                cycle = cycles
            }
            current = cycleSlideRotate(current)
            cycle++
        }

        return count(current)
    }

    val testInput1 = readInput("Day14_test1")
    check(part1(testInput1) == 136)

    val input = readInput("Day14")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}
