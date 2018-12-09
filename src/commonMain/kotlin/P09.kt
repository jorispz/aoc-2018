// 1: 383475
// 2: 3148209772
val p09 = fun() {

    val numPlayers = 465
    val lastMarblePoints = 7149800L

    // Gameboard is initialized with first marble
    val board = GameBoard()
    val marbles = (1..lastMarblePoints).asSequence()
    val scores = (0 until numPlayers).map { 0L }.toMutableList()

    var currentPlayer = 1
    for (marble in marbles) {
        scores[currentPlayer] += board.placeNext(marble)
        currentPlayer = (currentPlayer + 1) % numPlayers

    }

    scores.max().print { "The maximum score was $it" }
}

class GameBoard() {

    inner class Marble(var value: Long) {
        lateinit var next: Marble
        lateinit var previous: Marble
    }

    private var current = Marble(0)

    init {
        current.next = current
        current.previous = current
    }

    private fun moveForward(steps: Int) {
        repeat(steps) {
            current = current.next
        }
    }

    private fun moveBackward(steps: Int) {
        repeat(steps) {
            current = current.previous
        }
    }

    private fun deleteCurrent() {
        current.previous.next = current.next
        current.next.previous = current.previous
        current = current.next
    }

    private fun insert(new: Marble) {
        new.previous = current
        new.next = current.next

        current.next.previous = new
        current.next = new

        current = new
    }


    fun placeNext(value: Long) = if (value % 23 == 0L) {
        moveBackward(7)
        val score = current.value + value
        deleteCurrent()
        score
    } else {
        moveForward(1)
        insert(Marble(value))
        0
    }


}


val input_9_test = """..."""

val input_9 =
    """465 players; last marble is worth 71498 points"""