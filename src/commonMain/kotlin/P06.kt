import kotlin.math.abs

// 1: 5941
// 2: 40244
val p06 = fun() {
    val vertices = input_6.lines().map {
        val (x, y) = it.split(",").map { it.trim() }
        GridPoint(x.toInt(), y.toInt())
    }

    val (topLeft, bottomRight) = vertices.fold(Pair(vertices.first(), vertices.first())) { (tl, br), v ->
        Pair(GridPoint(minOf(tl.x, v.x), minOf(tl.y, v.y)), GridPoint(maxOf(br.x, v.x), maxOf(br.y, v.y)))
    }

    val grid = (topLeft.x..bottomRight.x).flatMap { x ->
        (topLeft.y..bottomRight.y).map { y ->
            GridPoint(x, y)
        }
    }

    val distances = grid.associateWith { p ->
        val distances = vertices.associateWith { it.dist(p) }
        val minDist = distances.minBy { it.value }!!.value
        val closest = distances.filterValues { it == minDist }.keys
        if (closest.count() == 1) {
            closest.single()
        } else {
            null
        }
    }

    val verticesWithInfiniteAreas = distances
        .filter { it.key.onEdge(topLeft, bottomRight) }
        .map { it.value }
        .distinct()

    vertices.filterNot { it in verticesWithInfiniteAreas }
        .associateWith { v -> distances.count { it.value == v } }
        .maxBy { it.value }
        .print { "Largest non-infinite area: ${it?.value}" }


    grid
        .associateWith { p ->
            vertices.sumBy { it.dist(p) }
        }
        .filter { it.value < 10000 }
        .count()
        .print { "Size of region: ${it}" }

}

data class GridPoint(val x: Int, val y: Int) {

    fun dist(other: GridPoint) = abs(x - other.x) + abs(y - other.y)

    fun onEdge(topLeft: GridPoint, bottomRight: GridPoint): Boolean {
        return x == topLeft.x || x == bottomRight.x || y == topLeft.y || y == bottomRight.y
    }
}


val input_6_test = """1, 1
1, 6
8, 3
3, 4
5, 5
8, 9"""

val input_6 = """183, 157
331, 86
347, 286
291, 273
285, 152
63, 100
47, 80
70, 88
333, 86
72, 238
158, 80
256, 140
93, 325
343, 44
89, 248
93, 261
292, 250
240, 243
342, 214
192, 51
71, 92
219, 63
240, 183
293, 55
316, 268
264, 151
68, 98
190, 288
85, 120
261, 59
84, 222
268, 171
205, 134
80, 161
337, 326
125, 176
228, 122
278, 151
129, 287
293, 271
57, 278
104, 171
330, 69
141, 141
112, 127
201, 151
331, 268
95, 68
289, 282
221, 359"""