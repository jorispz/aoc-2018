fun <T> T.print(msg: (T) -> Any? = { it }): T = this.apply { println(msg(this)) }


fun String.sorted(): String = this.toList().sorted().joinToString()

fun <T> List<T>.without(element: T): List<T> = this.filter { it != element }
fun <T> List<T>.replaceElementAt(index: Int, newValue: T): List<T> = this.toMutableList().apply { this[index] = newValue }
fun <T : Comparable<T>> Iterable<T>.maxWithIndex(): IndexedValue<T>? = this.withIndex().maxBy { it.value }

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) = Triple(this.first + other.first, this.second + other.second, this.third + other.third)

enum class Heading {
    N, S, E, W;

    fun right(): Heading = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    fun left(): Heading = when (this) {
        N -> W
        W -> S
        S -> E
        E -> N
    }

    fun reverse(): Heading = when (this) {
        N -> S
        S -> N
        E -> W
        W -> E
    }
}