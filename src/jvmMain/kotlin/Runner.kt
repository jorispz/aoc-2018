import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val day = args[0].toInt()
    val part = if (args.size == 2) args[1].toInt() else null
    val t = measureTimeMillis {
        Puzzles.run(day, part)
    }

    println("\nTime: $t ms")

}