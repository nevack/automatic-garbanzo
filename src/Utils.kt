import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * The cleaner shorthand for printing output with prefix.
 */
fun Any?.printlnPrefix(prefix: String) = println("$prefix: $this")

typealias Size = Pair<Int, Int>

fun findSize(input: List<String>): Size {
    val n = input.size
    val m = input.first().length

    input.forEach { line -> check(line.length == m) }

    return n to m
}

operator fun Size.contains(point: Pair<Int, Int>): Boolean {
    return point.first in 0..<first && point.second in 0..<second
}

fun <T> timed(prefix: String, block: () -> T) {
    print("$prefix: ")
    val measured = measureTime {
        print(block())
    }
    println(" (took ${measured.inWholeMilliseconds}ms)")
}
