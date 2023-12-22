import java.util.ArrayDeque
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun parseLine(line: String): Brick {
        val (a, b) = line.split("~").map { it.toP3() }
        return if (a.z > b.z) Brick(b, a) else Brick(a, b)
    }

    fun solver(input: List<String>): Pair<List<Set<Int>>, List<Set<Int>>> {
        val bricks = input.map { parseLine(it) }.sortedBy { it.a.z }

        // Top-view mapping (x, y) -> (max height, index of brick making that height)
        val height = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        // Mapping (index of brick) -> (layers of brick, indices of bricks supporting that brick)
        val pos = bricks.withIndex().map { (i, brick) ->
            val xs = min(brick.a.x, brick.b.x)..max(brick.a.x, brick.b.x)
            val ys = min(brick.a.y, brick.b.y)..max(brick.a.y, brick.b.y)

            val area = xs * ys

            val h = max(brick.a.z, brick.b.z) - min(brick.a.z, brick.b.z) + 1

            // Find first layer where we can drop this brick.
            // If we are looking from the top, it the highest value in the brick's area.

            val base = area.mapNotNull { (x, y) -> height[x to y] }
                .map { (z, _) -> z }
                .maxOrNull() ?: 0


            // Find all bricks we put our brick on top.
            // If we are looking from the top, it is all bricks in the area which have same height.
            val deps = area.mapNotNull { (x, y) -> height[x to y] }
                .filter { (z, _) -> z == base }
                .map { (_, owner) -> owner }
                .toSet()

            // Update top-view mapping height with new brick's height in the area
            area.forEach { (x, y) ->
                height[x to y] = base + h to i
            }

            base to deps
        }

        // Mapping (brick index) -> (indices of bricks supporting that brick)
        val depends = pos.map { (_, dependsOnBrickIndices) -> dependsOnBrickIndices }
        // Inverse mapping (brick index) -> (indices of bricks this brick supports)
        val support = pos.map { mutableSetOf<Int>() }

        depends.forEachIndexed { dependentBrickIndex, dependsOnBrickIndices ->
            for (brickIndex in dependsOnBrickIndices) {
                support[brickIndex].add(dependentBrickIndex)
            }
        }

        return depends to support
    }

    fun part1(input: List<String>): Int {
        val (depend, _) = solver(input)
        // At first, consider all brick are safe to remove
        val safeBrickIndices = depend.indices.toMutableSet()
        for (dependsOnBrickIndices in depend) {
            if (dependsOnBrickIndices.size == 1) {
                // If this brick is the only support for any other brick - it's not safe to remove
                safeBrickIndices -= dependsOnBrickIndices
            }
        }

        return safeBrickIndices.size
    }

    fun part2(input: List<String>): Int {
        val (depend, support) = solver(input)

        return depend.indices.sumOf { brickIndex ->
            val seenBricks = mutableSetOf(brickIndex)
            val queue = ArrayDeque<Int>().also { it.offer(brickIndex) }

            while (queue.any()) {
                val current = queue.pop()
                for (supportedBrickIndex in support[current]) {
                    if (supportedBrickIndex in seenBricks) continue
                    // Check whether there's any other unseen bricks supporting this bricks
                    if (depend[supportedBrickIndex].any { it !in seenBricks }) continue
                    // Add supported brick to chain reaction
                    queue.offer(supportedBrickIndex)
                    seenBricks.add(supportedBrickIndex)
                }
            }

            // Do not count itself
            seenBricks.size - 1
        }
    }

    val testInput = readInput("Day22_test1")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day22")
    timed("Part1 (impl 2) answer") { part1(input) }
    timed("Part2 (impl 2) answer") { part2(input) }
}

private typealias BrickIndex = Int

private data class P3(val x: Int, val y: Int, val z: Int)
private data class Brick(val a: P3, val b: P3)

private fun String.toP3() = split(",").map { it.toInt() }.let { (x, y, z) -> P3(x, y, z) }

private operator fun IntRange.times(other: IntRange): List<Pair<Int, Int>> = buildList{
    for (x in this@times) {
        for (y in other) {
            add(x to y)
        }
    }
}
