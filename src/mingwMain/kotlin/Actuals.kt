import kotlin.system.measureNanoTime

actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)

actual fun parseArgs(args: Array<String>): Triple<Int, Int, Int?> {
    val repeat = args[0].toInt()
    val day = args[1].toInt()
    val part = if (args.size == 3) args[2].toInt() else null
    return Triple(repeat, day, part)
}