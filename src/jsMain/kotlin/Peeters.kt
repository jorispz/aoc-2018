@file:Suppress("FunctionName")

package y2018.w3.d15

import y2018.w3.d15.Tile.*

val input = readText("y2018/w3/d15/input.txt")

sealed class Tile {
    object Wall : Tile()
    object Air : Tile()

    class Entity(val elf: Boolean, var pos: Point, var hp: Int) : Tile() {
        override fun toString() = (if (elf) "E" else "G") + "@$pos, ❤$hp"
    }
}

enum class Dir(val dx: Int, val dy: Int) {
    //reading order
    UP(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    DOWN(0, 1);
}

//optimized point
const val FACTOR = 100

typealias Point = Int

fun Point(x: Int, y: Int) = y * FACTOR + x

val Point.x get() = this % FACTOR
val Point.y get() = this / FACTOR

operator fun Point.plus(dir: Dir) = Point(x + dir.dx, y + dir.dy)

class Grid(val list: List<MutableList<Tile>>) {
    val height = list.size
    val width = list[0].size

    operator fun get(point: Point) =
        list.getOrNull(point.y)?.getOrNull(point.x) ?: Wall

    operator fun set(point: Point, tile: Tile) {
        list[point.y][point.x] = tile
    }

    override fun toString() = list.joinToString("\n") {
        it.joinToString("") {
            when {
                it == Wall -> "#"
                it == Air -> "."
                it is Entity && it.elf -> "E"
                else -> "G"
            }
        } + "   " + it.filterIsInstance<Entity>().joinToString { (if (it.elf) "E" else "G") + "(${it.hp})" }
    }
}

class Node(val pos: Point, val dist: Int, val prev: Node?, val step: Dir?)

fun calcMoveDir(entity: Entity, grid: Grid): Dir? {
    //don't move if you're next to an enemy already
    for (dir in Dir.values()) {
        val tile = grid[entity.pos + dir]
        if (tile is Entity && tile.elf != entity.elf)
            return null
    }

    //BFS
    val visited = BooleanArray(grid.height * FACTOR + grid.width)
    val toVisit = ArrayDeque<Node>()
    toVisit.add(Node(entity.pos, 0, null, null))

    val targets = mutableListOf<Node>()
    var smallestDist = -1

    bfs@ while (toVisit.isNotEmpty()) {
        val curr = toVisit.pollFirst()
        visited[curr.pos] = true
        if (smallestDist != -1 && curr.dist > smallestDist)
            break@bfs

        for (dir in Dir.values()) {
            val next = curr.pos + dir
            if (visited[next])
                continue

            val tile = grid[next]
            if (tile == Air) {
                toVisit.add(Node(next, curr.dist + 1, curr, dir))
            } else if (tile is Entity && tile.elf != entity.elf) {
                targets += curr
                smallestDist = curr.dist
            }
        }
    }

    val moveTarget = targets.minBy { it.pos }

    if (moveTarget != null && moveTarget.pos != entity.pos) {
        return generateSequence(moveTarget) { it.prev }
            .first { it.prev?.prev == null }.step
    }

    return null
}

fun runSimulation(elfAttack: Int, quitOnElfDeath: Boolean): SimResult? {
    val entities = mutableListOf<Entity>()

    val grid = Grid(input.lines().mapIndexed { y, line ->
        line.mapIndexedTo(mutableListOf()) { x, c ->
            when (c) {
                '#' -> Wall
                '.' -> Air
                'E' -> Entity(true, Point(x, y), 200).also { entities += it }
                'G' -> Entity(false, Point(x, y), 200).also { entities += it }
                else -> error("Illegal char $c")
            }
        }
    })

    var round = 0
    var prevChanged = true

    rounds@ while (true) {
        var currChanged = false

        for (entity in entities.sortedBy { it.pos }) {
            //skip dead entities
            if (entity !in entities)
                continue

            //stop if this entity can't find any targets
            if (entities.map { it.elf }.distinct().size < 2)
                break@rounds

            //Move
            //optimization: don't calc any moves if no one moved or died last round
            val moveDir = if (prevChanged || currChanged) calcMoveDir(entity, grid) else null
            if (moveDir != null) {
                currChanged = true
                grid[entity.pos] = Air
                entity.pos += moveDir
                grid[entity.pos] = entity
            }

            //Attack
            val fightTarget = Dir.values()
                .mapNotNull { grid[entity.pos + it] as? Entity }
                .filter { it.elf != entity.elf }
                .minBy { it.hp }

            if (fightTarget != null) {
                fightTarget.hp -= if (entity.elf) elfAttack else 3
                if (fightTarget.hp <= 0) {
                    if (fightTarget.elf && quitOnElfDeath)
                        return null

                    currChanged = true
                    entities -= fightTarget
                    grid[fightTarget.pos] = Air
                }
            }
        }

        round++
        prevChanged = currChanged
    }

    val totalHp = entities.sumBy { it.hp }
    return SimResult(round, totalHp, round * totalHp)
}

data class SimResult(val rounds: Int, val totalHp: Int, val outcome: Int)

fun main(args: Array<String>) {
    println("part 1: " + runSimulation(3, false))

    var attack = 3
    while (true) {
        attack++
        val result = runSimulation(attack, true) ?: continue
        println("part 2 " + result.outcome)
        break
    }
}