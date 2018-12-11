val p11 = fun() {


    val grid = (1..300).associateWith { x ->
        (1..300).associateWith { y ->
            FuelCell(x, y)
        }
    }

    fun power(corner: Pair<Int, Int>, squareSize: Int): Int {

        return (corner.first until corner.first + squareSize).sumBy { x ->
            (corner.second until corner.second + squareSize).sumBy { y ->
                grid.get(x)?.get(y)!!.power
            }
        }
    }

    fun maxPower(squareSize: Int): Triple<Pair<Int, Int>, Int, Int> {
        val corners = (1..(301 - squareSize)).flatMap { x -> (1..(301 - squareSize)).map { y -> Pair(x, y) } }

        val max = corners.associateWith {
            power(it, squareSize)
        }.maxBy { it.value }!!.toPair()
        return Triple(max.first, squareSize, max.second)

    }

    maxPower(3).print()

    (1..300).map {
        println(it)
        maxPower(it)
    }.maxBy { it.second }.print()

}


data class FuelCell(val x: Int, val y: Int) {

    val power by lazy {
        val rackId = x + 10
        val initial = rackId * y
        val increased = initial + serial
        val power = increased * rackId
        (power / 100).toString().last().toInt() - 5
    }

    fun power(squareSize: Int): Int {
        return (x..x + squareSize).sumBy { x ->
            (y..y + squareSize).sumBy { y ->
                val rackId = x + 10
                val initial = rackId * y
                val increased = initial + serial
                val power = increased * rackId
                (power / 100).toString().last().toInt() - 5
            }
        }
    }
}


val input_11_test = 8

val serial = 1718

