data class Puzzle(val day: Int, val part: Int? = null)

object Puzzles {

    private val puzzles = mapOf(
        Puzzle(1) to p01,
        Puzzle(2) to p02,
        Puzzle(3) to p03,
        Puzzle(4) to p04,
        Puzzle(5) to p05,
        Puzzle(6) to p06,
        Puzzle(7) to p07,
        Puzzle(8) to p08,
        Puzzle(9) to p09,
        Puzzle(10) to p10,
        Puzzle(11) to p11
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