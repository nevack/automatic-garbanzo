import org.intellij.lang.annotations.Language
import kotlin.io.path.*

val PREFIX = Path("src/")

@Language("kotlin")
val KT_TEMPLATE = """
fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("{day}_test1")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("{day}")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}
""".trimStart()

fun main(args: Array<String>) {
    require(args.size <= 1) { "Only one optional args is allowed - count of test cases to generate"}
    val testCaseCount = if (args.size == 1) {
        requireNotNull(args.first().toIntOrNull()) {
            "The only optional argument must be an integer number"
        }
    } else 1
    require(testCaseCount > 0) {
        "At least one test case must be generated"
    }

    val lastDay = PREFIX.listDirectoryEntries("Day*.kt")
        .map { it.name }
        .filter { it.startsWith("Day") && it.endsWith(".kt") }
        .map { it.substringAfter("Day").substringBefore(".kt") }
        .maxOfOrNull { it.toInt() } ?: 1

    val genDay = lastDay + 1
    val filename = "Day%02d".format(genDay)
    println("Generating for $filename")

    val ktFile = PREFIX.resolve("$filename.kt")
    ktFile.writeText(
        KT_TEMPLATE.replace("{day}", filename)
    )
    println("File created ${ktFile.toUri()}")

    PREFIX.resolve("$filename.txt").createFile()
    for (testCase in 1..testCaseCount) {
        PREFIX.resolve("${filename}_test${testCase}.txt").createFile()
    }

    println("Done!")
}
