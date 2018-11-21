data class Puzzle(val day: Int, val part: Int? = null)

object Puzzles {

    private val puzzles = mapOf(Puzzle(1) to p01, Puzzle(17) to p17)

    fun run(day: Int, part: Int? = null) {
        puzzles.filter { it.key == Puzzle(day, part) }.values.single()()
    }

}