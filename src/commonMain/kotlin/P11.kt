// 1: 243,43
// 2: 90,214,15

const val serial = 1718

val p11 = fun() {

    val grid = Array(300) { x ->
        IntArray(300) { y ->
            val rackId = (x + 1) + 10
            (((rackId * (y + 1)) + serial) * rackId).let {
                ((it / 100) % 10) - 5
            }
        }
    }

    fun power(corner: Pair<Int, Int>, squareSize: Int) = (corner.first until corner.first + squareSize).sumBy { x ->
        (corner.second until corner.second + squareSize).sumBy { y ->
            grid[x - 1][y - 1]
        }
    }


    fun maxPower(squareSize: Int): Triple<Pair<Int, Int>, Int, Int> {
        val corners = (1..(301 - squareSize)).flatMap { x -> (1..(301 - squareSize)).map { y -> Pair(x, y) } }

        val max = corners.associateWith {
            power(it, squareSize)
        }.maxBy { it.value }!!.toPair()
        return Triple(max.first, squareSize, max.second)

    }

    (1..300).map { squareSize ->
        maxPower(squareSize).also {
            if (squareSize == 3) {
                it.print { "Part 1: $it" }
            }
        }
    }.maxBy { it.third }.print { "Part 2: $it" }

}


