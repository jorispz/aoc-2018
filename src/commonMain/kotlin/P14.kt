// 1: 1044257397
// 2: 20185425
val p14 = fun() {

    recipeScores.drop(input_14).take(10).joinToString("").print { "Part 1: $it" }

    var result = mutableListOf<Int>()

    val iter = recipeScores.iterator()
    val target = input_14.toString()
    do {
        result.add(iter.next())
    } while (result.takeLast(target.length).joinToString("") != target)

    (result.size - target.length).print { "Part 2: $it" }


}

val recipeScores = sequence<Int> {
    var positions = listOf(0, 1)

    val recipes = mutableListOf(3, 7).onEach { yield(it) }
    while (true) {

        positions = positions.map {
            (it + 1 + recipes[it]) % recipes.size
        }

        val sum = positions.sumBy {
            recipes[it]
        }

        if (sum > 9) {
            (sum / 10).also {
                recipes.add(it)
                yield(it)
            }
        }

        (sum % 10).also {
            recipes.add(it)
            yield(it)
        }
    }
}

val input_14_test = """."""

val input_14 = 503761