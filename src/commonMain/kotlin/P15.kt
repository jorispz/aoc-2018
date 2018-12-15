import kotlin.math.abs

val p15 = fun() {

    val input = input_15_test_1
    val arena = Arena(input.lines().filterNot { it.isEmpty() })

    arena.run()

}

data class Position(val x: Int, val y: Int) {

    fun nextTo(): List<Position> =
        listOf(Position(x - 1, y), Position(x, y - 1), Position(x + 1, y), Position(x, y + 1))

    fun distanceTo(other: Position) = abs(x - other.x) + abs((y - other.y))

    fun adjacentTo(other: Position) = distanceTo(other) == 1
}

enum class ActorType {
    ELF, GOBLIN
}

class Actor(position: Position, val type: ActorType, val arena: Arena) {
    var position = position
        private set
    val strength: Int = 3
    var hitPoints: Int = 200
        private set
    val alive: Boolean
        get() = (hitPoints > 0)


    fun takeDamage(amount: Int) {
        hitPoints = (hitPoints - amount).coerceAtLeast(0)
    }

    fun attack(enemy: Actor) = enemy.takeDamage(strength).also { println("$this attacks $enemy") }

    fun moveTo(new: Position) {
        position = new
    }

    fun adjacentTo(other: Actor) = this.position.adjacentTo(other.position)


    fun act() {
        if (!alive) return

        val enemies = arena.enemiesFor(this)
        var adjacentEnemies = enemies.filter { it.adjacentTo(this) }

        if (adjacentEnemies.isEmpty()) {
            val poi = enemies.flatMap { arena.openPositionsNextTo(it) }.distinct()
            val paths = poi.mapNotNull { arena.shortestPath(position, it) }
            paths.whenNotEmpty {
                val shortestLength = it.minBy { it.size }!!.size
                val nextPosition = it.filter { it.size == shortestLength }
                    .map { it.first() }
                    .sortedWith(compareBy(Position::y, Position::x))
                    .first()
                moveTo(nextPosition)

            }
            adjacentEnemies = enemies.filter { it.adjacentTo(this) }
        }

        if (adjacentEnemies.isNotEmpty()) {
            this.attack(
                adjacentEnemies.sortedWith(
                    compareBy(
                        { it.hitPoints },
                        { it.position.y },
                        { it.position.x })
                ).first()
            )
        }


    }

    override fun toString(): String {
        return "$type at $position ($hitPoints)"
    }

}


class Arena(input: List<String>) {
    private val height = input.size
    private val width = input.maxBy { it.length }!!.length
    private val map = Array(height) { BooleanArray(width) }
    private val actors = mutableListOf<Actor>()
    private val survivors
        get() = actors.filter { it.alive }
    private val emptySpaces: List<Position>
    private var roundsCompleted = 0

    init {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '#' -> {
                        map[x][y] = false
                    }
                    '.' -> {
                        map[x][y] = true
                    }
                    'E' -> {
                        map[x][y] = true
                        actors.add(Actor(Position(x, y), ActorType.ELF, this))
                    }
                    'G' -> {
                        map[x][y] = true
                        actors.add(Actor(Position(x, y), ActorType.GOBLIN, this))
                    }
                    else -> error("Unknown map element $c")

                }

            }
        }
        emptySpaces =
                (0 until height).flatMap { y -> (0 until width).map { x -> if (map[x][y]) Position(x, y) else null } }
                    .filterNotNull()
    }

    fun draw() {
        val sb = StringBuilder((height + 1) * width)
        sb.append("\n")
        for (y in 0 until height) {
            for (x in 0 until width) {
                val actor = survivors.singleOrNull { it.position.x == x && it.position.y == y }
                when (actor?.type) {
                    ActorType.ELF -> sb.append("E")
                    ActorType.GOBLIN -> sb.append("G")
                    else -> sb.append(if (map[x][y]) "." else "#")
                }
            }
            sb.append("\n")
        }
        println(sb.toString())
    }

    fun enemiesFor(actor: Actor) = survivors.without(actor).filter { it.type != actor.type }


    fun openPositionsNextTo(actor: Actor): List<Position> {
        return actor.position.nextTo()
            .filter { p -> p in emptySpaces && survivors.without(actor).none { it.position == p } }
    }

    fun shortestPath(from: Position, to: Position): List<Position>? {
        // TODO implement proper shortes path
        return null
    }

    fun run() {
        while (survivors.distinctBy { it.type }.count() > 1) {
            survivors.forEach {
                it.act()
            }
            roundsCompleted++
            draw()
        }
        println("Done in $roundsCompleted rounds")
    }


}

operator fun Array<BooleanArray>.get(position: Position): Boolean = this[position.x][position.y]


val input_15_test_1 = """
#######
#.GE..#
#.....#
#.....#
#.....#
#.....#
#######
"""

val input_15 = """
################################
###############.##...###########
##############..#...G.#..#######
##############.............#####
###############....G....G......#
##########..........#..........#
##########................##..##
######...##..G...G.......####..#
####..G..#G...............####.#
#######......G....G.....G#####E#
#######.................E.######
########..G...............######
######....G...#####E...G....####
######..G..G.#######........####
###.........#########.......E.##
###..#..#...#########...E.....##
######......#########.......####
#####...G...#########.....######
#####G......#########.....######
#...#G..G....#######......######
###...##......#####.......######
####..##..G........E...E..######
#####.####.....######...########
###########..#...####...E.######
###############...####..#...####
###############...###...#.E.####
#####################.#E....####
#####################.#...######
###################...##.#######
##################..############
##################...###########
################################
"""
