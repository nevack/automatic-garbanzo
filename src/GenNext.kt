import org.intellij.lang.annotations.Language
import kotlin.io.path.*

val PREFIX = Path("src/")

@Language("kotlin")
val KT_TEMPLATE = """
fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readInput("{day}_test1")
    check(part1(testInput1) == 0)

    val input = readInput("{day}")
    part1(input).printlnPrefix("Part1 answer")
}
""".trimStart()

fun main() {
    val lastDay = PREFIX.listDirectoryEntries("Day*.kt")
        .map { it.name }
        .filter { it.startsWith("Day") && it.endsWith(".kt") }
        .map { it.substringAfter("Day").substringBefore(".kt") }
        .maxOfOrNull { it.toInt() } ?: 1

    val genDay = lastDay + 1
    val filename = "Day%02d".format(genDay)
    println("Generating for $filename")

    PREFIX.resolve("$filename.kt").writeText(
        KT_TEMPLATE.replace("{day}", filename)
    )
    PREFIX.resolve("$filename.txt").createFile()
    PREFIX.resolve("${filename}_test1.txt").createFile()
    PREFIX.resolve("${filename}_test2.txt").createFile()

    println("Done!")
}
