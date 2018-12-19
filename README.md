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

# Day 09
Part 2 only, as this is just part 1 but with more iterations
```
JVM     911,  849,  858,  804,  580,  677,  754,  802,  878,  645,  718,  566,  783,  791,  674,  745,  659,  690,  843,  653,  636,  647,  588,  614,  563
JS     7972, 5725, 7785, 6902, 5954, 7249, 7683, 6372, 6798, 5879, 6393, 6482, 6961, 7328, 7099, 7267, 6660, 6630, 7021, 6667, 7325, 7344, 7020, 5753, 6491
MinGW  DNF - Process finished with exit code -1,073,741,571.
```

# Day 10
```
JVM     262,  125,  111,   67,   51,   46,   44,   52,   65,   48,   63,   68,   55,   53,   87,   72,   69,   59,   61,  119,   74,   68,   56,   67,  114
JS      454,  321,  432,  378,  428,  284,  262,  267,  263,  257,  254,  259,  259,  253,  258,  257,  254,  264,  257,  258,  260,  263,  255,  256,  261
MinGW  5830, 6231, 6296, 6273, 6445, 6465, 6451, 6610, 6593, 6360, 6393, 6598, 6351, 5959, 6210, 6709, 6418, 6041, 6226, 6067, 6589, 6148, 6213, 6125, 6110
```

# Day 11
```
JVM     103,  59,  49,  44,  43,  48,  39,  39,  48,  46,  43,  39,  39,  40,  44,  43,  56,  42,  39,  57,  71,  67,  56,  47,  42
JS      114, 114,  91,  84, 103,  84,  93,  98, 102,  89, 136, 108, 183,  91, 110, 142, 143, 110, 107, 101,  85,  79,  78,  78,  89
MinGW   495, 476, 473, 471, 470, 477, 468, 470, 479, 473, 468, 473, 475, 472, 470, 472, 478, 470, 470, 563, 637, 529, 478, 487, 481
```

# Day 12
```
JVM     171,  22,  29,  32,  27,  27,  40,  33,  37,  31,  28,  31,  28,  38,  30,  32,  19,  18,  18,  19,   19,   19,  19,   42,  33
JS      599, 663, 913, 604, 411, 391, 434, 371, 378, 391, 413, 377, 376, 380, 389, 375, 436, 414, 386, 383,  387,  383, 381,  375, 384
MinGW   964, 882, 896, 872, 842, 856, 819, 817, 826, 911, 849, 918, 867, 905, 911, 862, 858, 864, 932, 990, 1062, 1123, 919, 1007, 914
```

# Day 13
```
JVM      185,  41,   55,   41,   21,  21,   21,   22,   20,   20,   20,   19,   19,   21,   20,   20,   20,   20,   23,   20,   13,   14,   19,   17,  20
JS       206, 147,  131,  124,  106, 131,  107,  150,  108,  152,  159,  126,  127,  103,   93,   81,   80,   77,   79,   75,   78,   75,  124,   84,  77
MinGW   1048, 999, 1001, 1047, 1001, 995, 1084, 1079, 1312, 1080, 1047, 1062, 1015, 1016, 1016, 1276, 1242, 1045, 1016, 1095, 1140, 1157, 1063, 1035, 997
```

# Day 15
Part 1 only, Part 2 is just multiple iterations of part 1
```
JVM     2577,  1588,  1440,   814,   707,   653,   629,   679,    654,   715,   643,   630,   640,   625,   644,   711,   628,   621,   632,   641,   624,   641,   631,   733,   625
JS     96180, 91861, 91553, 94814, 92427, 97929, 98084, 96629, 101512, 91892, 93183, 93240, 93145, 93284, 93140, 93008, 92464, 93455, 92053, 93561, 92639, 92330, 91834, 92319, 93662
MinGW  28506, 28514, 29037, 27034, 27130, 26862, 26765, 27248,  31764, 33451, 27041, 31480, 26902, 26986, 26955, 27487, 27932, 27127, 27879, 27555, 27217, 27077, 26918, 26804, 27221
```

# Day 16

Today for the first time I got this warning while running the native version, which I say online but never experienced myself before:
```
C:\BuildAgent\work\4d622a065c544371\runtime\src\main\cpp\Memory.cpp:1150: runtime assert: Memory leaks found
```
So, memory leaks! Does'nt seem to hurt the performance too much though:

```
JVM     236,  37,  43,  51,  48,  47,  44,  32,  44,  47,  41,  45,  37,  39,  46,  55,  56,  17,  14,  10,   9,  12,  14,  21,  17
JS      194, 117,  88,  66,  53,  60,  63,  71,  70,  74,  94,  73,  65, 115,  57,  57,  80,  77,  76, 110,  66,  79,  67,  76,  68
MinGW   227, 213, 190, 185, 189, 190, 189, 198, 195, 197, 195, 199, 189, 192, 190, 191, 189, 186, 184, 195, 201, 197, 188, 191, 193
```

# Day 17

```
JVM    256,  69,  51,  42,  28,  33,  27,  24,  24,  19,  26,  32,  27,  32,  35,  28,  37,  27,  31,  23,  35,  38,  38,  28,  22
JS     316, 215, 275, 155, 241, 140, 253, 164, 189, 130, 156, 119, 158, 308, 135, 256, 152, 243, 181, 173, 159, 178, 149, 154, 138
MinGW  649, 476, 576, 796, 393, 660, 473, 685, 507, 552, 449, 639, 548, 484, 632, 462, 639, 508, 678, 516, 663, 636, 488, 712, 497
```

# Day 18

```
JVM      936,   660,   670,   399,   393,   398,   414,   631,   452,   508,   442,   543,   536,   478,   518,   619,   390,   393,   373,   540,   516,   503,   380,   375,   407
JS      8966,  8843,  8529,  8862,  8659,  8739,  9221,  9050,  9056,  9358,  8671,  8787,  9288,  9496,  8686,  8856,  8386,  9171,  8723,  8853,  8332,  8968,  9214, 11370, 10733
MinGW  17518, 19046, 16335, 17301, 18215, 16456, 15979, 15913, 15972, 16208, 16022, 15879, 15866, 16364, 15811, 16003, 16193, 16233, 16099, 16227, 19049, 19641, 18687, 19536, 16219
```

# Day 19
```
JVM     330,  192,  178,  281,  237,  266,  282,  206,  217,  162,  164,  166,  164,  168,  169,  176,  154,  153,  156,  150,  165,  160,  158,  153,  155
JS      560,  692,  714,  488,  490,  484,  498,  492,  490,  497,  491,  493,  486,  512,  499,  495,  488,  490,  486,  492,  492,  486,  491,  483,  486
MinGW  5020, 5464, 4903, 4964, 5159, 5203, 5172, 5252, 5159, 5106, 4870, 5215, 4994, 4986, 4946, 4978, 4985, 4980, 4999, 4983, 4983, 4985, 5016, 5243, 5001
```