val p14 = fun() {

//    recipeScores.drop(input_14).take(10).joinToString("").print()
    recipeScores.take(14).joinToString("").print()

    var result = mutableListOf<Int>()

    val iter = recipeScores.iterator()
    val target = input_14.toString()
    do {
        result.add(iter.next())
    } while (result.takeLast(target.length).joinToString("") != target)

    (result.size - target.length).print()


}

val recipeScores = sequence<Int> {
    val elves = listOf(Elf(0), Elf(1))

    val recipes = mutableListOf(3, 7).onEach { yield(it) }
    while (true) {
        val sum = elves.sumBy { elf ->
            elf.move(recipes)
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


data class Elf(var position: Int) {
    fun move(recipes: List<Int>): Int {
        position = (position + 1 + recipes[position]) % recipes.size
        return recipes[position]
    }
}


val input_14_test = """."""

val input_14 = 503761