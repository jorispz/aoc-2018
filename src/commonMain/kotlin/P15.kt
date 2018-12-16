import kotlin.math.abs

val p15 = fun() {

    val input = input_15
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
        println("$this moves to $new")
        position = new
    }

    fun adjacentTo(other: Actor) = this.position.adjacentTo(other.position)


    fun act() {
        if (!alive) return

        val enemies = arena.enemiesFor(this)
        var adjacentEnemies = enemies.filter { it.adjacentTo(this) }

        if (adjacentEnemies.isEmpty()) {
            val poi = enemies
                .flatMap { arena.openPositionsNextTo(it) }
                .distinct()
                .associateWith { arena.shortestPaths(position, it) }
                .filterValues { it.isNotEmpty() }

            if (poi.values.any { it.isNotEmpty() }) {
                val shortestPathLength = poi.values.minBy { it.first().size }!!.first().size
                val target = poi.filter { it.value.first().size == shortestPathLength }.keys.sortedWith(
                    compareBy(
                        Position::y,
                        Position::x
                    )
                ).first()

                val next = poi[target]!!.map { it.first() }.sortedWith(
                    compareBy(
                        Position::y,
                        Position::x
                    )
                ).first()
                moveTo(next)
                adjacentEnemies = enemies.filter { it.adjacentTo(this) }
            }

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

    fun battleOver(): Boolean = survivors.distinctBy { it.type }.count() == 1

    fun draw() {
        println()
        survivors.forEach { println(it) }
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

    fun enemiesFor(actor: Actor) = survivors.filter { it.type != actor.type }


    fun openPositionsNextTo(actor: Actor): List<Position> {
        return actor.position.nextTo()
            .filter { p -> p in emptySpaces && survivors.without(actor).none { it.position == p } }
    }

    fun shortestPaths(from: Position, to: Position): List<List<Position>> {

        val vertices = emptySpaces.filterNot { p -> survivors.any { it.position == p } }
            .map { Vertex(it, Int.MAX_VALUE, mutableSetOf()) }.toMutableSet()
        vertices.add(Vertex(from, 0, mutableSetOf()))

        // Early return if either to or from are not in the set of vertices
        if (!vertices.any { it.position == to } || !vertices.any { it.position == from }) {
            return emptyList()
        }

        vertices.single { it.position == from }.dist = 0
        val shortestPathSet = mutableSetOf<Vertex>()

        var done = false
        while (!done) {
            val current = vertices.minBy { it.dist }!!
            if (current.dist == Int.MAX_VALUE) {
                done = true
            } else {
                vertices.remove(current)
                done = vertices.isEmpty()
                vertices.filter { it.position.adjacentTo(current.position) }.forEach { vertex ->
                    val newDist = current.dist + 1
                    if (newDist < vertex.dist) {
                        vertex.dist = newDist
                        vertex.parents.clear()
                        vertex.parents.add(current)
                    } else if (newDist == vertex.dist) {
                        vertex.parents.add(current)
                    }
                }
                shortestPathSet.add(current)
            }
        }

        val target = shortestPathSet.singleOrNull { it.position == to } ?: return emptyList()

        var result = listOf(listOf(target))
        while (result.first().last().parents.isNotEmpty()) {
            result = result.flatMap { path -> path.last().parents.map { parent -> path + parent } }
        }

        return result.map { it.map { it.position }.reversed() }.filter { it.first() == from }.map { it.drop(1) }
    }

    fun run() {
        draw()
        while (!battleOver()) {
            survivors.sortedWith(
                compareBy(
                    { it.position.y },
                    { it.position.x })
            ).forEach {
                it.act()
            }
            if (!battleOver()) {
                roundsCompleted++
            }

            draw()
        }
        println("Done in $roundsCompleted rounds")
        println("Total number of hitpoints: ${survivors.sumBy { it.hitPoints }}")
        println("Part 1: ${roundsCompleted * survivors.sumBy { it.hitPoints }}")
    }


}

operator fun Array<BooleanArray>.get(position: Position): Boolean = this[position.x][position.y]
class Vertex(val position: Position, var dist: Int, val parents: MutableSet<Vertex>)


val input_15_test_1 = """
#######
#.G...#
#.....#
#.....#
#...E.#
#.....#
#######
"""

val input_15_test_2 = """
#########
#G..G..G#
#.......#
#.......#
#G..E..G#
#.......#
#.......#
#G..G..G#
#########
"""

val input_15_test_3 = """
#######
#.G...#
#...EG#
#.#.#G#
#..G#E#
#.....#
#######
"""

val input_15_test_4 = """
#######
#G..#E#
#E#E.E#
#G.##.#
#...#E#
#...E.#
#######
"""

val input_15_test_5 = """
#######
#E.G#.#
#.#G..#
#G.#.G#
#G..#.#
#...E.#
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
