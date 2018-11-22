data class Insert(val position: Int, val value: Int)

val p17 = fun() {
    var pos = 0
    var step = 1
    fun inserts(stepSize: Int = 328) = generateSequence(Insert(0, 0)) {
        pos = 1 + ((pos + stepSize) % step)
        Insert(pos, step++)
    }

    val seq = inserts()

    val stack = mutableListOf(listOf(0))
    seq.take(2018).forEach { insert ->
        val current = stack.last()
        val next = (current.take(insert.position) + insert.value + current.takeLast(current.size - insert.position))
        stack.add(next)
    }
    stack.last()[stack.last().indexOf(2017) + 1].print { "Value directly following 2017 after 2018 inserts: $it" }

    seq.take(50_000_000).filter { it.position == 1 }.last().print { "Last value inserted after 0: ${it.value}" }
}
