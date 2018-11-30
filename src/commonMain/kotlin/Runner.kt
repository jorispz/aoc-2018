data class Puzzle(val day: Int, val part: Int? = null)

object Puzzles {

    private val puzzles = mapOf(
        Puzzle(1) to p01
    )

    fun run(day: Int, part: Int? = null) {
        puzzles.filter { it.key == Puzzle(day, part) }.values.single()()
    }

}

fun main(args: Array<String>) {

    val (repeat, day, part) = parseArgs(args)
    val times = (1..repeat).map {
        measureNanos {
            Puzzles.run(day, part)
        }
    }

    println("\nTimes: ${times.map { (it / 1e6).toInt() }.joinToString()} ms")
}

expect inline fun measureNanos(block: () -> Unit): Long
expect fun parseArgs(args: Array<String>): Triple<Int, Int, Int?>