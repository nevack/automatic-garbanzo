fun main() {
    fun parse(input: List<String>): List<List<String>> = buildList {
        var result = mutableListOf<String>()
        for (line in input) {
            if (line.isEmpty()) {
                add(result)
                result = mutableListOf()
                continue
            }
            result.add(line)
        }
        if (result.isNotEmpty()) add(result)
    }

    fun solve(input: List<String>, threshold: Int = 0): Int {
        val fields = parse(input)
        var sum = 0
        for (field in fields) {
            val (n, m) = findSize(field)
            // Scan each vertical mirror line
            for (mirrorLine in 1..<m) {
                var mismatched = 0
                for (j in 0..<minOf(m - mirrorLine, mirrorLine)) {
                    for (i in 0..<n) {
                        if (field[i][mirrorLine - j - 1] != field[i][mirrorLine + j])
                            mismatched++
                    }
                }
                if (mismatched == threshold)
                    sum += mirrorLine
            }
            // Scan each horizontal mirror line
            for (mirrorLine in 1..<n) {
                var mismatched = 0
                for (i in 0..<minOf(n - mirrorLine, mirrorLine)) {
                    for (j in 0..<m) {
                        if (field[mirrorLine - i - 1][j] != field[mirrorLine + i][j])
                            mismatched++
                    }
                }
                if (mismatched == threshold)
                    sum += 100 * mirrorLine
            }
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        return solve(input)
    }

    fun part2(input: List<String>): Int {
        return solve(input, threshold = 1)
    }

    val testInput1 = readInput("Day13_test1")
    check(part1(testInput1) == 405)
    val testInput2 = readInput("Day13_test1")
    check(part2(testInput2) == 400)

    val input = readInput("Day13")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}
