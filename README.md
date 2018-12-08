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

Usually, I try to finish a puzzle as quickly as possible, then spend some time to cleanup and optimise the code. If and when these optimizations alter the performance, I'll update the measurements below to reflect the new solution.

# Important
There is a very explicit warning in the [FAQ of Kotlin/Native](https://github.com/JetBrains/kotlin-native/blob/master/RELEASE_NOTES.md#performance) that says the current version of Kotlin/Native is not suited for performance analysis, as it has not been optimised in any way for performance and benchmarking. 

Apart from that, all measurements were taken on my laptop, under non-controlled circumstances using non-optimized code and tools. So, **if you use the results below for anything important, you're insane...**



# Day 01
The answer to today's challenge show quite a dramatic difference in performance between the platforms, with native an order of magnitude slower than JVM and JS. The table below shows the duration of the calculation in `ms`.

It's clear to see JVM's just-in-time compiler do its magic, with duration dropping rapidly after the first iteration.

```
JVM    101,  18,  44,  10,  27,  12,  23,  10,  27,  10,  10,  10,  12,  53,  12,  14,  19,  14,  44,  13,  10,  11,  13,  15,  16
JS     107,  59,  50,  65,  79,  58,  60,  79,  46,  59,  79,  68,  45,  58, 104,  74,  72,  90,  91,  65,  57,  76,  55,  74, 115 
MinGW  413, 362, 362, 368, 360, 359, 362, 362, 370, 363, 384, 405, 369, 380, 382, 379, 472, 365, 362, 382, 386, 364, 362, 364, 371 
```

My first solution to the second part of the puzzle used a `List` instead of a `Set` to keep track of frequencies. This turned out to be dramatically slower, with each iteration taking 15+ seconds on the JVM.

# Day 02
On day 2 we see a similar picture as Day 1:

```
JVM    136,  31,  31,  27,  28,  27,  25,  25,  25,  25,  19,  16,  19,  16,  17,  19,  16,  17,  26,  17,  16,  14,  14,  14,  14
JS     278, 172, 126, 130, 159, 157, 185, 136, 197, 182, 159, 123, 117, 118, 118, 119, 118, 114, 115, 118, 116, 116, 116, 116, 133 
MinGW  702, 657, 658, 648, 648, 648, 636, 627, 636, 631, 629, 625, 625, 623, 622, 621, 619, 622, 622, 623, 625, 622, 626, 621, 621 
```

# Day 03
Performance differences between platforms are again as expected, with (after warm-up) the JVM taking `127ms` in its best run, NodeJS `933ms` and MinGW `4433ms`

```
JVM     479,  305,  346,  292,  325,  302,  348,  285,  130,  142,  214,  196,  192,  190,  195,  133,  126,  129,  234,  127,  132,  132,  255,  190,  200
JS     1568, 1222, 1031,  970, 1003, 1004,  960, 1040, 1093,  988,  987,  989,  933, 1017, 1026,  951, 1015,  932, 1036, 1010,  955, 1009, 1045, 1012,  973
MinGW  4556, 4433, 4451, 4886, 4449, 4512, 4812, 5660, 4808, 4828, 4612, 4870, 4735, 4658, 4700, 4971, 4602, 4817, 4652, 5319, 4771, 4466, 5031, 5498, 5299
```

# Day 04

```
JVM    132, 30, 21, 15, 15,  8,  6,  8,  8,  8,  8,  7,  8,  9,  9,  5,  7,  8,  8,  8,  7,  6,   4,   4,  4
JS     150, 97, 44, 32, 34, 24, 21, 20, 23, 20, 29, 28, 20, 19, 20, 28, 18, 21, 22, 20, 19, 20,  26,  30, 31
MinGW  109, 93, 78, 78, 78, 78, 93, 63, 93, 78, 78, 78, 93, 78, 78, 93, 78, 78, 78, 79, 93, 78, 109, 109, 125
```

# Day 05

```
JVM      90,  30,  52,  28,  15,  14,  15,  19,  18,  17,  17,  16,  16,  16,  16,  21,  18,  19,  17,  22,  20,  24,  21,  25,  18
JS      415, 269, 366, 381, 366, 279, 273, 275, 222, 251, 259, 246, 221, 262, 241, 224, 245, 243, 236, 241, 240, 244, 311, 227, 243
MinGW   329, 249, 267, 234, 251, 234, 250, 218, 219, 281, 218, 235, 329, 218, 218, 234, 249, 252, 265, 235, 250, 242, 265, 265, 266
```

# Day 06
```
JVM      1159,   475,   737,   338,   298,   292,   288,   323,   371,   344,   322,   299,   279,   354,   376,   349,   302,   279,   357,   339,   309,   283,   270,   345,   329
JS       3692,  3075,  3065,  3075,  3075,  3013,  3020,  3106,  3082,  3018,  3143,  3303,  3070,  3102,  3077,  3037,  3002,  3097,  3075,  3061,  3072,  3013,  3074,  3023,  3075
MinGW   15513, 13786, 14103, 13545, 13373, 13354, 13994, 13405, 13460, 13600, 13693, 13249, 13455, 13172, 12965, 13361, 12916, 13182, 13226, 13561, 12944, 14516, 13039, 13106, 13562
```

# Day 07
```
JVM     108,  18,  19, 14, 18, 19, 17, 17, 16, 14, 26, 13, 12, 10, 10, 13, 11,  8,  8,  8,   8,  8,  8,  8,  8
JS      146,  95,  67, 48, 54, 52, 48, 43, 47, 56, 56, 56, 46, 41, 48, 88, 52, 54, 56, 61, 147, 56, 51, 57, 59
MinGW   114, 107, 101, 95, 97, 97, 99, 95, 92, 95, 94, 97, 95, 95, 93, 98, 92, 96, 95, 93,  94, 96, 99, 98, 97
```

# Day 08
```
JVM     61,  6,  6, 13,  5,  4,  5,  3,  5,  5, 14,  4,  2,  2,  2,  2,  2,  3,  3,  3,  3,  4,  2,  2,  2
JS      62, 26, 14, 10,  4,  5,  4,  4,  4,  5,  6,  5,  5,  6,  5,  5,  6,  5,  5,  5,  5,  6,  7,  8,  6
MinGW   47, 47, 45, 46, 40, 48, 40, 41, 38, 39, 38, 48, 52, 47, 47, 40, 41, 42, 43, 43, 41, 41, 43, 43, 40
```