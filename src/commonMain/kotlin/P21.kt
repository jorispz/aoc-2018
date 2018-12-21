// Part 1: minimum amount of instructions executed when 6619857
// Part 2: maximum amount of instructions executed when 9547924
val p21 = fun() {

    val results = mutableListOf(0L)
    do {
        var x = (results.last() or 65536) * 256
        var next = 9010242L

        do {
            x /= 256
            next += (x and 255)
            next = (((next and 16777215) * 65899) and 16777215)
        } while (x >= 256)

        results.add(next)

    } while (results.indexOf(next) == results.size - 1)

    println("Part 1: minimum amount of instructions executed when ${results[1]}")
    println("Part 2: maximum amount of instructions executed when ${results[results.size - 2]}")

}

fun decompile_21() {

    var a = 0L
    var b = 0L
    var c = 0L
    var d = 0L
    var e = 0L
    var f = 0L

    // #ip 2
    // 0 1 2 3 4 5
    // a b c d e f


    f = 123                     //  0: seti 123 0 5
    f = f and 456               //  1: bani 5 456 5
    f = if (f == 72L) 1 else 0   //  2: eqri 5 72 5
    c += c + f                  //  3: addr 5 2 2
    c = 0                       //  4: seti 0 0 2
    f = 0                       //  5: seti 0 3 5
    d = f or 65536              //  6: bori 5 65536 3
    f = 9010242                 //  7: seti 9010242 6 5
    b = d and 255               //  8: bani 3 255 1
    f += b                      //  9: addr 5 1 5
    f = f and 16777215          // 10: bani 5 16777215 5
    f = f * 65899               // 11: muli 5 65899 5
    f = f and 16777215          // 12: bani 5 16777215 5
    b = if (256 > d) 1 else 0   // 13: gtir 256 3 1
    c = c + b                   // 14: addr 1 2 2
    c = c + 1                   // 15: addi 2 1 2
    c = 27                      // 16: seti 27 6 2
    b = 0                       // 17: seti 0 8 1
    e = b + 1                   // 18: addi 1 1 4
    e = e * 256                 // 19: muli 4 256 4
    e = e * 256                 // 20: gtrr 4 3 4
    c = c + e                   // 21: addr 4 2 2
    c = c + 1                   // 22: addi 2 1 2
    c = 25                      // 23: seti 25 5 2
    b = b + 1                   // 24: addi 1 1 1
    c = 17                      // 25: seti 17 7 2
    d = b                       // 26: setr 1 3 3
    c = 7                       // 27: seti 7 2 2
    b = if (a == f) 1 else 0    // 28: eqrr 5 0 1
    c = c + b                   // 29: addr 1 2 2
    c = 5                       // 30: seti 5 2 2


}

val input_21 = """
#ip 2
seti 123 0 5
bani 5 456 5
eqri 5 72 5
addr 5 2 2
seti 0 0 2
seti 0 3 5
bori 5 65536 3
seti 9010242 6 5
bani 3 255 1
addr 5 1 5
bani 5 16777215 5
muli 5 65899 5
bani 5 16777215 5
gtir 256 3 1
addr 1 2 2
addi 2 1 2
seti 27 6 2
seti 0 8 1
addi 1 1 4
muli 4 256 4
gtrr 4 3 4
addr 4 2 2
addi 2 1 2
seti 25 5 2
addi 1 1 1
seti 17 7 2
setr 1 3 3
seti 7 2 2
eqrr 5 0 1
addr 1 2 2
seti 5 2 2
""".trimIndent()