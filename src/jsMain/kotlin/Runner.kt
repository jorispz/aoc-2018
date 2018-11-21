import kotlin.math.roundToLong

external val process: dynamic
fun main() {
    val args = (process["argv"] as Array<String>).drop(2)
    val day = args[0].toInt()
    val part = if (args.size == 2) args[1].toInt() else null
    val t = measureTimeMillis {
        Puzzles.run(day, part)
    }

    println("\nTime: $t ms")
}

inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val millis = (end[0] * 1000 + end[1] / 1e6) as Double
    return millis.roundToLong()
}