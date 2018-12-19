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
        Puzzle(11) to p11,
        Puzzle(12) to p12,
        Puzzle(13) to p13,
        Puzzle(14) to p14,
        Puzzle(15) to p15,
        Puzzle(16) to p16,
        Puzzle(17) to p17,
        Puzzle(18) to p18,
        Puzzle(19) to p19
    )

    fun run(day: Int, part: Int? = null) {
        puzzles[Puzzle(day, part)]!!()
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