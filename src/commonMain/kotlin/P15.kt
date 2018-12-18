// Part 1: 229798 (elf power 3)
// Part 2: 52972 (elf power 19)

const val ELF_POWER = 3

val p15 = fun() {

//    val input = input_15_test_3 // 47 x 590 = 27730
//    val input = input_15_test_4 // 37 x 982 = 36334
//    val input = input_15_test_5 // 35 x 793 = 27755
//    val input = input_15_test_6 // 46 x 859 = 39514
//    val input = input_15_test_7 // 54 * 536 = 28944
//    val input = input_15_test_8 // 20 * 937 = 18740
    val input = input_15_test_from_reddit_1 // 68 * 2812 = 191216

    val arena = Arena(input.lines().filterNot { it.isEmpty() })

    arena.run()

}


enum class ActorType {
    ELF, GOBLIN
}

class Actor(position: Position, val type: ActorType, val arena: Arena) {
    var position = position
        private set
    val strength: Int = if (type == ActorType.ELF) ELF_POWER else 3
    var hitPoints: Int = 200
        private set
    val alive: Boolean
        get() = (hitPoints > 0)


    private fun takeDamage(amount: Int) {
        hitPoints = (hitPoints - amount).coerceAtLeast(0)
    }

    private fun attack(enemy: Actor) = enemy.takeDamage(strength).also { println("$this attacks $enemy") }

    private fun moveTo(new: Position) {
        println("$this moves to $new")
        position = new
    }

    private fun adjacentTo(other: Actor) = this.position.adjacentTo(other.position)


    fun act() {
        if (!alive || arena.battleOver()) return

        val enemies = arena.enemiesFor(this)
        var adjacentEnemies = enemies.filter { it.adjacentTo(this) }

        if (adjacentEnemies.isEmpty()) {
            val paths = arena.shortestPathsToNearestEnemy(this, enemies)

            if (paths.isNotEmpty()) {
                val next = paths.map { it.first() }.sortedWith(
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
        println("Rounds completed: ${roundsCompleted}")
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


    private fun openPositionsInRange(actor: Actor): List<Position> {
        return actor.position.adjacents()
            .filter { p -> p in emptySpaces && survivors.without(actor).none { it.position == p } }
    }

    fun shortestPathsToNearestEnemy(actor: Actor, enemies: List<Actor>): List<List<Position>> {
        val from = actor.position
        val targetPositions = enemies.flatMap { openPositionsInRange(it) }.distinct()

        val unvisited = emptySpaces.minus(survivors.map { it.position })
            .map { Vertex(it, Int.MAX_VALUE, mutableListOf()) }
            .plus(Vertex(from, 0, mutableListOf()))
            .toMutableList()

        val visited = mutableListOf<Vertex>()
        val targets = unvisited.filter { v -> v.position in targetPositions }

        var done = false
        var currentDist = 0
        while (!done) {

            val nextToVisit = unvisited.filter { it.dist == currentDist }

            if (nextToVisit.isEmpty()) {
                done = true
            } else {
                unvisited.removeAll(nextToVisit)
                nextToVisit.forEach { current ->
                    unvisited.filter { it.position.adjacentTo(current.position) }.forEach { vertex ->
                        vertex.dist = current.dist + 1
                        vertex.parents.add(current)
                    }
                    visited.add(current)
                }
                done = unvisited.isEmpty() || targets.any { it in visited }
            }
            currentDist++
        }

        val selectedTarget = targets
            .filter { it in visited }
            .sortedWith(compareBy({ it.position.y }, { it.position.x }))
            .firstOrNull()
            ?: return emptyList()

        var paths = listOf(listOf(selectedTarget))
        while (paths.first().last().parents.isNotEmpty()) {
            paths = paths.flatMap { path -> path.last().parents.map { parent -> path + parent } }
        }
        return paths.map { it.map { it.position }.reversed() }.filter { it.first() == from }.map { it.drop(1) }

    }

    fun run() {
        draw()
        while (!battleOver()) {
            val actors = survivors.sortedWith(
                compareBy(
                    { it.position.y },
                    { it.position.x })
            )

            for (actor in actors.dropLast(1)) {
                actor.act()
            }
            if (!battleOver()) {
                actors.last().act()
                if (battleOver()) {
                    roundsCompleted++
                }
            }

            if (!battleOver()) {
                roundsCompleted++
            }

            draw()
        }

        println("Done in $roundsCompleted rounds with elf power $ELF_POWER")
        println("Total number of hitpoints: ${survivors.sumBy { it.hitPoints }}")
        println("Points: ${roundsCompleted * survivors.sumBy { it.hitPoints }}")
        println("Dead elves: ${actors.filter { it.type == ActorType.ELF && !it.alive }.count()}")
    }


}

operator fun Array<BooleanArray>.get(position: Position): Boolean = this[position.x][position.y]
class Vertex(val position: Position, var dist: Int, val parents: MutableList<Vertex>)


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

val input_15_test_6 = """
#######
#E..EG#
#.#G.E#
#E.##E#
#G..#.#
#..E#.#
#######
"""

val input_15_test_7 = """
#######
#.E...#
#.#..G#
#.###.#
#E#G#G#
#...#G#
#######
"""

val input_15_test_8 = """
#########
#G......#
#.E.#...#
#..##..G#
#...##..#
#...#...#
#.G...G.#
#.....G.#
#########
"""

val input_15_test_from_reddit_1 = """
################################
#################.....##########
#################..#.###########
#################.........######
##################......########
#################G.GG###########
###############...#..###########
###############......G..########
############..G.........########
##########.G.....G......########
##########......#.........#..###
##########...................###
#########G..G.#####....E.G.E..##
######..G....#######...........#
#######.....#########.........##
#######..#..#########.....#.####
##########..#########..G.##..###
###########G#########...E...E.##
#########.G.#########..........#
#########GG..#######.......##.E#
######.G......#####...##########
#...##..G..............#########
#...#...........###..E.#########
#.G.............###...##########
#................###############
##.........E.....###############
###.#..............#############
###..G........E.....############
###......E..........############
###......#....#E#...############
###....####.#...##.#############
################################
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
