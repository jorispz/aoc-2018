// 1: 243,43
// 2: 90,214,15

const val serial = 1718
const val gridSize = 300

val p11 = fun() {

    val sat = summedAreaTable(gridSize) { x, y ->
        val rackId = x + 10
        (((rackId * y) + serial) * rackId).let {
            ((it / 100) % 10) - 5
        }
    }

    (1..gridSize).map { size ->
        val best = sat.bestSquare(size)
        if (size == 3) println("Part 1: $best")
        Triple(best.first, size, best.second)
    }.maxBy { it.third }.print { "Part 2: $it" }

}

fun summedAreaTable(gridSize: Int, f: (Int, Int) -> Int) =
    Array(gridSize + 1) { IntArray(gridSize + 1) }.apply {
        (1..gridSize).forEach { x -> this[x][1] = f(x, 1) + this[x - 1][1] }
        (1..gridSize).forEach { y -> this[1][y] = f(1, y) + this[1][y - 1] }
        (2..gridSize).forEach { x ->
            (2..gridSize).forEach { y ->
                this[x][y] = f(x, y) + this[x - 1][y] + this[x][y - 1] - this[x - 1][y - 1]
            }
        }
    }

fun Array<IntArray>.powerOfSquare(x: Int, y: Int, s: Int): Int {
    val l = s - 1
    return this[x - 1][y - 1] + this[x + l][y + l] - this[x - 1][y + l] - this[x + l][y - 1]
}

fun Array<IntArray>.bestSquare(size: Int): Pair<GridPoint, Int> {
    val xs = (1..gridSize - size + 1).asSequence()

    return xs.flatMap { x ->
        xs.map { y ->
            Pair(GridPoint(x, y), this.powerOfSquare(x, y, size))
        }
    }.maxBy { it.second }!!
}

fun Array<IntArray>.print(pad: Int = 3) {
    val width = this.size
    val height = this[0].size
    (0 until height).forEach { y ->
        println((0 until width).joinToString(" ") { x -> this[x][y].toString().padStart(pad) })
    }
}



