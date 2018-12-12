// 1: 2736
// 2:
val p12 = fun() {

    val masks = input_12_rules.lines().map { line ->
        val mask = line.substring(0..4).map { it == '#' }
        val result = line.last() == '#'
        Pair(mask, result)
    }

    var current = input_12_initial.map { it == '#' }
    var indexOfStart = 0
    val falses = listOf(false, false, false, false, false)

    val history = mutableListOf<List<Boolean>>()
    val historyOfIndex = mutableListOf<Int>()

    repeat(20) {
        history.add(current)
        historyOfIndex.add(indexOfStart)
        current = falses + current + falses
        indexOfStart -= 5

        current = current.windowed(5, 1) { l ->
            masks.first { m -> m.first == l }.second
        }
        indexOfStart += 2

        val indexOfFirstPlant = current.indexOf(true)
        indexOfStart += indexOfFirstPlant
        current = current.subList(current.indexOf(true), current.lastIndexOf(true) + 1)
    }




    current.mapIndexed { i, b -> if (b) i + indexOfStart else 0 }.sum().print()

}


data class Pot(val index: Int, val hasPlant: Boolean)


fun List<Boolean>.draw(): List<Boolean> {
    println(map { if (it) "#" else "." }.joinToString(""))
    return this
}


val input_12_initial =
    """#..######..#....#####..###.##..#######.####...####.##..#....#.##.....########.#...#.####........#.#."""

val input_12_rules = """#...# => .
##.## => .
###.. => .
.#### => .
#.#.# => .
##..# => #
..#.# => #
.##.. => #
##... => #
#..## => #
#..#. => .
.###. => #
#.##. => .
..### => .
.##.# => #
....# => .
##### => .
#.### => .
.#..# => .
#.... => .
...## => .
.#.## => .
##.#. => #
#.#.. => #
..... => .
.#... => #
...#. => #
..#.. => .
..##. => .
###.# => .
####. => .
.#.#. => ."""
