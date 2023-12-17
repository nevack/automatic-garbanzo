import java.util.PriorityQueue

fun main() {
    fun part1(input: List<String>): Int {
        val (n, m) = findSize(input)
        val end = n - 1 to m - 1

        val seen = mutableSetOf<Node>()
        val queue = PriorityQueue<Entry>()
        queue.offer(Entry(0, Node(0 to 0, Direction.RIGHT, 0)))

        while (queue.any()) {
            val entry = queue.poll()
            if (entry.node in seen) continue
            seen += entry.node

            if (entry.node.position == end) {
                return entry.cost
            }

            val nextDirections = mutableListOf<Direction>()
            if (entry.node.count < 3) {
                nextDirections += entry.node.direction
            }
            nextDirections += entry.node.direction + 1
            nextDirections += entry.node.direction - 1

            for (direction in nextDirections) {
                val nextPos = entry.node.position moveTo direction
                if (nextPos.first !in 0..<n || nextPos.second !in 0..<m) continue

                val nextCount = 1 + if (direction == entry.node.direction) entry.node.count else 0

                val next = Node(nextPos, direction, nextCount)
                if (next in seen) continue

                val nextCost = entry.cost + input[nextPos.first][nextPos.second].digitToInt()
                queue.offer(Entry(nextCost, next))
            }
        }

        return 0
    }

    val testInput1 = readInput("Day17_test1")
    check(part1(testInput1) == 102)

    val input = readInput("Day17")
    part1(input).printlnPrefix("Part1 answer")
}

data class Node(
    val position: Pair<Int, Int>,
    val direction: Direction,
    val count: Int,
)

data class Entry(
    val cost: Int,
    val node: Node,
): Comparable<Entry> {
    override fun compareTo(other: Entry): Int {
        return cost - other.cost
    }
}
