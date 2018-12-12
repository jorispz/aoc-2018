// 1: 2736
// 2: 3150000000905
val p12 = fun() {

    val startingPots = input_12_initial.mapIndexed { i, c -> Pot(i.toLong(), c == '#') }.draw()
    val rules = input_12_rules.lines().map { line ->
        val mask = line.substring(0..4).map { it == '#' }
        val result = line.last() == '#'
        Rule(mask, result)
    }

    // Part 1
    (1..20).fold(startingPots) { pots, _ ->
        pots.evolve(rules)
    }.map { if (it.hasPlant) it.index else 0 }.sum().print { "Part 1: $it" }


    // Part 2
    var n = 0
    var previous = startingPots
    var current = previous
    do {
        previous = current
        current = previous.evolve(rules)
        n++
    } while (!current.isEquivalentTo(previous))

    val shiftPerStep = current.first().index - previous.first().index
    val stepsToGo = (50000000000L - n)
    val shift = stepsToGo * shiftPerStep
    current.map { if (it.hasPlant) it.index + shift else 0 }.sum().print { "Part 2: $it" }

}


data class Pot(val index: Long, val hasPlant: Boolean)
data class Rule(val mask: List<Boolean>, val result: Boolean)

fun List<Pot>.isEquivalentTo(other: List<Pot>): Boolean = this.map { it.hasPlant } == other.map { it.hasPlant }

fun List<Pot>.evolve(rules: List<Rule>): List<Pot> {
    return expand().windowed(5, 1) { l ->
        val hasPlant = rules.first { r -> r.mask == l.map { it.hasPlant } }.result
        Pot(l[2].index, hasPlant)
    }.compress()
}

fun List<Pot>.expand(): List<Pot> {
    val startIndex = this.first().index
    val endIndex = this.last().index
    return (startIndex - 5 until startIndex).map {
        Pot(
            it,
            false
        )
    } + this + (endIndex + 1 until endIndex + 6).map { Pot(it, false) }
}

fun List<Pot>.compress(): List<Pot> {
    val startIndex = this.indexOfFirst { it.hasPlant }
    val endIndex = this.indexOfLast { it.hasPlant }
    return this.subList(startIndex, endIndex + 1)
}

fun List<Pot>.draw(): List<Pot> {
    println(joinToString("") { if (it.hasPlant) "\ud83c\udf32" else "." })
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
