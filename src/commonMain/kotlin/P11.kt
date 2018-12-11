// 1: 243,43
// 2: 90,214,15

// Uses https://en.wikipedia.org/wiki/Summed-area_table

const val serial = 1718
const val gridSize = 300

val p11 = fun() {

    val sat = summedAreaTable(gridSize) { x, y ->
        val rackId = x + 10
        (((rackId * y) + serial) * rackId).let {
            ((it / 100) % 10) - 5
        }
    }

    var best = Triple(0, 0, 0)
    for (size in 1..gridSize) {
        sat.bestSquare(size).also {
            if (size == 3) println("Part 1: $it")

            if (it.third > best.third) {
                best = it
            }
        }
    }
    best.print { "Part 2: $it" }

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
    return this[x - 1][y - 1] + this[x + (s - 1)][y + (s - 1)] - this[x - 1][y + (s - 1)] - this[x + (s - 1)][y - 1]
}

fun Array<IntArray>.bestSquare(size: Int): Triple<Int, Int, Int> {
    var best = Triple(0, 0, 0)
    for (x in 1..gridSize - size + 1) {
        for (y in 1..gridSize - size + 1) {
            powerOfSquare(x, y, size).also {
                if (it > best.third) {
                    best = Triple(x, y, it)
                }
            }
        }
    }

    return best
}

fun Array<IntArray>.print(pad: Int = 3) {
    val width = this.size
    val height = this[0].size
    (0 until height).forEach { y ->
        println((0 until width).joinToString(" ") { x -> this[x][y].toString().padStart(pad) })
    }
}



