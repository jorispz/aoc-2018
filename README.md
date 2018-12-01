# Advent of Code 2018 in Multi-Platform Kotlin

## Intro
In 2018, I will be participating in the [Advent of Code](https://adventofcode.com) using [Kotlin multi-platform](https://kotlinlang.org/docs/reference/multiplatform.html) code only. This means I can run my solutions on the JVM, on Node JS and natively on Windows through MinGW.

I will be measuring the performance of each platform by measuring the elapsed time calculating the solution `N` times:
```kotlin
val times = (1..repeat).map {
    measureNanos {
        Puzzles.run(day, part)
    }
}
```
where `repeat` is the number of desired repetitions, and `day` and `part` are used to select the proper challenge.

Note that the `measureNanos` function is the first example of functionality that isn't available in common code, since measuring time is platform-specific. The function is implemented using Kotlin's `expext/actual` mechanism.

`Runner.kt` defines its expectation of a function `measureNanos` for each supported platform with the signature:
```kotlin
expect inline fun measureNanos(block: () -> Unit): Long
```

Each platform provides an actual implementation in files called `Actuals.kt`. On the JVM and native, we can use `measureNanoTime` as provided by the Kotlin standard library on these platforms:

JVM and Native:
```kotlin
actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)
```

On Node, we need to write our own implementation:
```kotlin
actual inline fun measureNanos(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val nanos = (end[0] * 1e9 + end[1]) as Double
    return nanos.roundToLong()
}
```
**NB** All measurements were taken on my laptop, under non-controlled circumstances using non-optmized code and tools. If you use the results below for anything important, you're insane...

# Day 01
The answer to today's challenge show quite a dramatic difference in performance between the platforms, with native an order of magnitude slower than JVM and JS. The table below shows the duration of the calculation in `ms`.

It's clear to see JVM's just-in-time compiler do its magic, with duration dropping rapidly after the first iteration.
 

```
JVM     97,  18,  52,  11,  39,  16,  29,  14,  27,  14,  12,  13,  15,  49,  15,  14,  16,  16,  53,  11,  11,  11,  12,  10,  12
JS     102,  83,  61,  83,  71,  65,  62,  53,  81,  45,  71,  82,  87,  57,  46, 108,  59,  70,  49,  56, 147,  58,  51,  95,  75 
MinGW  447, 435, 447, 420, 390, 408, 452, 419, 426, 395, 403, 453, 406, 407, 437, 407, 439, 426, 380, 415, 397, 432, 394, 460, 442 
```
