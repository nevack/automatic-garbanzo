fun main() {
    fun parseLine(line: String): Pair<String, List<Int>> {
        val (pattern, groupsStr) = line.split(" ")
        val groups = groupsStr.split(",").map { it.toInt() }
        return pattern to groups
    }

    // i - current position in dots group
    // j - current position in blocks group
    // k - current length of blocks group
    fun solve(dp: Array<Array<LongArray>>, pattern: String, groups: IntArray, i: Int, j: Int, k: Int): Long {
        // Hit end of pattern
        if (i >= pattern.length) {
            return if (j >= groups.size || j == groups.size - 1 && k == groups[j]) 1 else 0
        }
        // Already calculated
        if (dp[i][j][k] >= 0) return dp[i][j][k]
        val char = pattern[i]
        var res = 0L
        // Update with next assumed to be a dot
        if (char == '.' || char == '?') {
            if (k > 0 && groups[j] == k) {
                // Previous was blocks, move to next blocks group
                res += solve(dp, pattern, groups, i + 1, j + 1, 0)
            } else if (k == 0) {
                // Previous was dots, stay in current blocks group
                res += solve(dp, pattern, groups, i + 1, j, 0)
            }
        }
        // Update with next assumed to be a block
        if (char == '#' || char == '?') {
            if (j < groups.size && k < groups[j]) {
                // Stay in current blocks group, grow current length
                res += solve(dp, pattern, groups, i + 1, j, k + 1)
            }
        }
        // Save calculated
        dp[i][j][k] = res
        return res
    }

    fun part1(input: List<String>): Long {
        return input.map { line -> parseLine(line) }.sumOf { (pattern, groups) ->
            val dp = Array(pattern.length) {
                Array(groups.size + 1) { j ->
                    LongArray(groups.getOrElse(j) { 0 } + 1) { -1 }
                }
            }
            solve(dp, pattern, groups.toIntArray(), 0, 0, 0)
        }
    }

    fun part2(input: List<String>, repeat: Int = 5): Long {
        return input.map { line -> parseLine(line) }.map { (pattern, groups) ->
            val patternRepeated = Array(repeat) { pattern }.joinToString("?")
            val groupsRepeated = buildList { repeat(repeat) { addAll(groups) } }
            patternRepeated to groupsRepeated
        }.sumOf { (pattern, groups) ->
            val dp = Array(pattern.length) {
                Array(groups.size + 1) { j ->
                    LongArray(groups.getOrElse(j) { 0 } + 1) { -1 }
                }
            }
            solve(dp, pattern, groups.toIntArray(), 0, 0, 0)
        }
    }

    val testInput1 = readInput("Day12_test1")
    check(part1(testInput1) == 21L)
    check(part2(testInput1) == 525152L)

    val input = readInput("Day12")
    part1(input).printlnPrefix("Part1 answer")
    part2(input).printlnPrefix("Part2 answer")
}
