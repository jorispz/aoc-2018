import kotlin.math.min

// 1: 4479
// 2: 1032
val p22 = fun() {

    val depth = 4002
    val target = Position(5, 746)

    val cave = Cave(depth, target)
    cave.riskLevel().print { "Part 1: total risk level is $it" }

    val unvisited = mutableMapOf<Pair<Region, Equipment>, Int>().also {
        it[Pair(cave[0, 0]!!, Equipment.TORCH)] = 0
    }
    val visited = mutableSetOf<Pair<Region, Equipment>>()

    while (true) {
        val current = unvisited.minBy { it.value }!!
        unvisited.remove(current.key)
        visited.add(current.key)

        val (region, equipment) = current.key
        val dist = current.value

        if (region.pos == target && equipment == Equipment.TORCH) {
            dist.print { "Part 2: Shortest distance to target is $it" }
            break
        }

        val nextBySwitchGear =
            listOf(Pair(region, region.toolsAllowed().single { it != equipment })).filter { it !in visited }
        nextBySwitchGear.forEach { key ->
            unvisited[key] = min(dist + 7, unvisited.getOrPut(key) { Int.MAX_VALUE })
        }

        val nextByMove = cave.neighboursOf(region).filter { equipment in it.toolsAllowed() }.map { Pair(it, equipment) }
            .filter { it !in visited }
        nextByMove.forEach { key ->
            unvisited[key] = min(dist + 1, unvisited.getOrPut(key) { Int.MAX_VALUE })
        }

    }

}

enum class RegionType(val riskLevel: Int) {
    ROCKY(0), WET(1), NARROW(2)
}

data class Region(val pos: Position, val geoIndex: Long, val erosionLevel: Long, val type: RegionType) {
    fun toolsAllowed(): List<Equipment> = when (type) {
        RegionType.ROCKY -> listOf(Equipment.CLIMB, Equipment.TORCH)
        RegionType.WET -> listOf(Equipment.CLIMB, Equipment.NONE)
        RegionType.NARROW -> listOf(Equipment.NONE, Equipment.TORCH)
    }

}

class Cave(val depth: Int, val targetPostion: Position) {

    private val maxX = targetPostion.x + 50
    private val maxY = targetPostion.y + 50

    private val regions = Array(maxX + 1) { Array<Region?>(maxY + 1) { null } }

    private fun createRegion(x: Int, y: Int): Region {
        val position = Position(x, y)
        val geoIndex = if ((x == 0 && y == 0) || position == targetPostion) {
            0L
        } else {
            when {
                y == 0 -> x * 16807L
                x == 0 -> y * 48271L
                else -> get(x - 1, y)!!.erosionLevel * get(x, y - 1)!!.erosionLevel
            }
        }
        val erosionLevel = (geoIndex + depth) % 20183L
        val type = when (erosionLevel % 3) {
            0L -> RegionType.ROCKY
            1L -> RegionType.WET
            2L -> RegionType.NARROW
            else -> error("Unpossible")
        }
        return Region(position, geoIndex, erosionLevel, type)

    }

    fun neighboursOf(r: Region): List<Region> {
        return listOfNotNull(
            this[r.pos.x - 1, r.pos.y],
            this[r.pos.x + 1, r.pos.y],
            this[r.pos.x, r.pos.y - 1],
            this[r.pos.x, r.pos.y + 1]
        )
    }


    operator fun get(x: Int, y: Int): Region? {
        return if (x < 0 || x > maxX || y < 0 || y > maxY) {
            null
        } else {
            regions[x][y] ?: createRegion(x, y).also { regions[x][y] = it }
        }
    }

    operator fun set(x: Int, y: Int, region: Region) {
        regions[x][y] = region
    }

    operator fun get(pos: Position) = get(pos.x, pos.y)
    operator fun set(pos: Position, region: Region) = set(pos.x, pos.y, region)

    fun riskLevel(): Int {
        return (0..targetPostion.x).flatMap { x ->
            (0..targetPostion.y).map { y ->
                this[x, y]?.type?.riskLevel
            }
        }.filterNotNull().sum()
    }

}

enum class Equipment {
    TORCH, CLIMB, NONE
}

class RouteNode(val region: Region, val equipment: Equipment, var dist: Int = Int.MAX_VALUE)