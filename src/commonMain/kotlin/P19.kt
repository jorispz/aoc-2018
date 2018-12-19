// 1:     1848
// 2: 22157688
val p19 = fun() {
    val input = input_19.lines()
    val boundRegister = input.first().split(" ")[1].toInt()
    val program = input.drop(1).map {
        val (op, a, b, c) = it.split(" ")
        Inst(ops[op]!!, a.toInt(), b.toInt(), c.toInt())
    }

    val registers = MutableList(6) { 0 }
    registers[0] = 0
    var pointer = 0

    do {
        registers[boundRegister] = pointer
        program[pointer](registers)
        pointer = registers[boundRegister] + 1
    } while (pointer in 0 until program.size)

    registers[0].print { "Part 1: $it" }

    // This is the kotlin version of what happens when you
    // run the program with a = 1
    val b = 10551260
    var a = 0
    for (d in 1..b) {
        if (b % d == 0) {
            a += d
        }
    }
    a.print { "Part 2: sum of all divisors of $b is $it" }


}

val decompile = fun() {
    var a = 1
    var b = 0
    var c = 0
    var d = 0
    var e = 0
    var f = 0

    // 0 1 2 3 4 5
    // a b c d e f

    e += 16                     //  0: addi 4 16 4
    d = 1                       //  1: seti 1 9 3
    c = 1                       //  2: seti 1 6 2
    f = c * d                   //  3: mulr 3 2 5
    f = if (f == b) 1 else 0    //  4: eqrr 5 1 5
    e += f                      //  5: addr 5 4 4
    e += 1                      //  6: addi 4 1 4
    a += d                      //  7: addr 3 0 0
    c += 1                      //  8: addi 2 1 2
    f = if (c > b) 1 else 0     //  9: gtrr 2 1 5
    e += f                      // 10: addr 4 5 4
    e = 2                       // 11: seti 2 9 4
    d += 1                      // 12: addi 3 1 3
    f = if (d > b) 1 else 0     // 13: gtrr 3 1 5
    e += f                      // 14: addr 5 4 4
    e = 1                       // 15: seti 1 0 4
    e *= e                      // 16: mulr 4 4 4
    b += 2                      // 17: addi 1 2 1
    b *= b                      // 18: mulr 1 1 1
    b = b * e                   // 19: mulr 4 1 1
    b *= 11                     // 20: muli 1 11 1
    f++                         // 21: addi 5 1 5
    f *= e                      // 22: mulr 5 4 5
    f += 2                      // 23: addi 5 2 5
    b += f                      // 24: addr 1 5 1
    e += a                      // 25: addr 4 0 4
    e = 0                       // 26: seti 0 1 4
    f = e                       // 27: setr 4 3 5
    f *= e                      // 28: mulr 5 4 5
    f += e                      // 29: addr 4 5 5
    f *= e                      // 30: mulr 4 5 5
    f *= 14                     // 31: muli 5 14 5
    f *= e                      // 32: mulr 5 4 5
    b += f                      // 33: addr 1 5 1
    a = 0                       // 34: seti 0 6 0
    e = 0                       // 35: seti 0 7 4

}


data class Inst(val op: Operation, val a: Int, val b: Int, val c: Int) {
    operator fun invoke(registers: Registers) {
        op(registers, a, b, c)
    }
}

val ops = mapOf<String, Operation>(
    "addr" to { r, a, b, c -> r[c] = r[a] + r[b] },
    "addi" to { r, a, b, c -> r[c] = r[a] + b },
    "mulr" to { r, a, b, c -> r[c] = r[a] * r[b] },
    "muli" to { r, a, b, c -> r[c] = r[a] * b },
    "banr" to { r, a, b, c -> r[c] = r[a] and r[b] },
    "bani" to { r, a, b, c -> r[c] = r[a] and b },
    "borr" to { r, a, b, c -> r[c] = r[a] or r[b] },
    "bori" to { r, a, b, c -> r[c] = r[a] or b },
    "setr" to { r, a, _, c -> r[c] = r[a] },
    "seti" to { r, a, _, c -> r[c] = a },
    "gtir" to { r, a, b, c -> r[c] = if (a > r[b]) 1 else 0 },
    "gtri" to { r, a, b, c -> r[c] = if (r[a] > b) 1 else 0 },
    "gtrr" to { r, a, b, c -> r[c] = if (r[a] > r[b]) 1 else 0 },
    "eqir" to { r, a, b, c -> r[c] = if (a == r[b]) 1 else 0 },
    "eqri" to { r, a, b, c -> r[c] = if (r[a] == b) 1 else 0 },
    "eqrr" to { r, a, b, c -> r[c] = if (r[a] == r[b]) 1 else 0 }
)

val input_19_test = """
#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5
""".trimIndent()

val input_19 = """
#ip 4
addi 4 16 4
seti 1 9 3
seti 1 6 2
mulr 3 2 5
eqrr 5 1 5
addr 5 4 4
addi 4 1 4
addr 3 0 0
addi 2 1 2
gtrr 2 1 5
addr 4 5 4
seti 2 9 4
addi 3 1 3
gtrr 3 1 5
addr 5 4 4
seti 1 0 4
mulr 4 4 4
addi 1 2 1
mulr 1 1 1
mulr 4 1 1
muli 1 11 1
addi 5 1 5
mulr 5 4 5
addi 5 2 5
addr 1 5 1
addr 4 0 4
seti 0 1 4
setr 4 3 5
mulr 5 4 5
addr 4 5 5
mulr 4 5 5
muli 5 14 5
mulr 5 4 5
addr 1 5 1
seti 0 6 0
seti 0 7 4
""".trimIndent()