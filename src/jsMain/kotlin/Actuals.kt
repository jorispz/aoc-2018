import kotlin.math.roundToLong

external val process: dynamic

actual inline fun measureNanos(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val nanos = (end[0] * 1e9 + end[1]) as Double
    return nanos.roundToLong()
}

actual fun parseArgs(args: Array<String>): Triple<Int, Int, Int?> {
    val args = (process["argv"] as Array<String>).drop(2)
    val repeat = args[0].toInt()
    val day = args[1].toInt()
    val part = if (args.size == 3) args[2].toInt() else null
    return Triple(repeat, day, part)
}