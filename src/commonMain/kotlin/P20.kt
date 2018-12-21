val p20 = fun() {

    val building = Building()
    val iter = input_20_test_1.drop(1).dropLast(1).iterator()
    building.draw()

}

data class Location(val x: Int, val y: Int) {
    fun east(): Location = this.copy(x = x + 1)
    fun west(): Location = this.copy(x = x - 1)
    fun north(): Location = this.copy(y = y - 1)
    fun south(): Location = this.copy(y = y + 1)
}

class Building() {
    private val rooms = mutableMapOf<Location, Room>()
    val start = Room(Location(0, 0)).also { rooms[it.location] = it }

    inner class Room(
        val location: Location,
        var north: Room? = rooms[location.north()],
        var south: Room? = rooms[location.south()],
        var east: Room? = rooms[location.east()],
        var west: Room? = rooms[location.west()]
    ) {
        override fun toString(): String {
            return "Room(loc=$location)"
        }

        fun expandEast(): Room = with(this.location.east()) {
            rooms.getOrPut(this) { Room(this) }
        }

        fun expandWest(): Room = with(this.location.west()) {
            rooms.getOrPut(this) { Room(this) }
        }

        fun expandNorth(): Room = with(this.location.north()) {
            rooms.getOrPut(this) { Room(this) }
        }

        fun expandSouth(): Room = with(this.location.south()) {
            rooms.getOrPut(this) { Room(this) }
        }


        operator fun get(location: Location) = rooms[location]
        operator fun get(x: Int, y: Int) = rooms[Location(x, y)]

        operator fun set(location: Location, room: Room) {
            rooms[location] = room
        }

        operator fun set(x: Int, y: Int, room: Room) {
            rooms[Location(x, y)] = room
        }
    }

    fun draw() {
        val roomLocations = rooms.values.map { it.location }
        val minX = roomLocations.minBy { it.x }!!.x - 1
        val maxX = roomLocations.maxBy { it.x }!!.x + 1
        val minY = roomLocations.minBy { it.y }!!.y - 1
        val maxY = roomLocations.maxBy { it.y }!!.y + 1

        val sb = StringBuilder()
        sb.append("\n")
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (Location(x, y) in roomLocations) {
                    sb.append(".")
                } else {
                    sb.append("#")
                }
            }
            sb.append("\n")
        }
        sb.append("\n")
        println(sb.toString())
    }
}


val input_20_test_1 = "^ENWWW(NEEE|SSE(EE|N))\$"