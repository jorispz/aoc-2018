// 1: AJZNXHKE
// 2: 10905
val p10 = fun() {
    val pattern = Regex("""position=<\s*(-?\d.*),\s*(-?\d.*)> velocity=<\s*(-?\d.*),\s*(-?\d.*)>""")
    val particles = input_10.lines()
        .map { line ->
            val (x, y, vx, vy) = pattern.matchEntire(line)!!.groupValues.drop(1)
            Particle(x.toInt(), y.toInt(), vx.toInt(), vy.toInt())
        }

    var current = particles
    var ticks = 0
    do {
        current = current.map { it.move() }
        ticks++
    } while (!current.showsText())

    current.draw()
    ticks.print { "It took $it iterations to reach this point" }
}

data class Particle(val x: Int, val y: Int, val vX: Int, val vY: Int) {

    fun move(): Particle = copy(x = x + vX, y = y + vY)

}

fun List<Particle>.showsText(): Boolean {
    val minY = minBy { it.y }!!.y
    val maxY = maxBy { it.y }!!.y
    // Note that this stop condition was determined by using
    // <= N with increasingly lower N,: 100, 50, 30, 20 etc
    // The idea is that readable text has the lowest line height possible
    // In the example it is eight...
    //
    // Undoubtedly there are more intelligent stop conditions, like
    // basic OCR
    return (maxY - minY) == 9
}

fun List<Particle>.draw() {
    val minX = minBy { it.x }!!.x
    val maxX = maxBy { it.x }!!.x
    val minY = minBy { it.y }!!.y
    val maxY = maxBy { it.y }!!.y

    println()
    (minY..maxY).map { _y ->
        (minX..maxX).joinToString("") { _x ->
            if (this.any { it.x == _x && it.y == _y }) {
                "#"
            } else {
                " "
            }
        }
    }.forEach { println(it) }
    println()
}


val input_10 = """position=< 11063,  22004> velocity=<-1, -2>
position=< 32870, -21613> velocity=<-3,  2>
position=< 43767,  22003> velocity=<-4, -2>
position=< 32865, -10705> velocity=<-3,  1>
position=< 21984, -43419> velocity=<-2,  4>
position=< 43775,  11100> velocity=<-4, -1>
position=< 43770, -43425> velocity=<-4,  4>
position=< 32881,  22004> velocity=<-3, -2>
position=< 32899,  11102> velocity=<-3, -1>
position=< 43764,  11098> velocity=<-4, -1>
position=< 43805,  32912> velocity=<-4, -3>
position=< 54680, -32516> velocity=<-5,  3>
position=< 32851,  11107> velocity=<-3, -1>
position=< 54696,  22012> velocity=<-5, -2>
position=< 43794,  43816> velocity=<-4, -4>
position=<-54388, -43427> velocity=< 5,  4>
position=<-43465,  43815> velocity=< 4, -4>
position=< 11036,  32916> velocity=<-1, -3>
position=< 43798,  54726> velocity=<-4, -5>
position=<-10722,  54727> velocity=< 1, -5>
position=< 32897, -54331> velocity=<-3,  5>
position=< 11043, -32520> velocity=<-1,  3>
position=< 32868, -10706> velocity=<-3,  1>
position=<-10760,  54718> velocity=< 1, -5>
position=<-43449, -21611> velocity=< 4,  2>
position=< 43751,  11105> velocity=<-4, -1>
position=<-21625,  43817> velocity=< 2, -4>
position=< 54685,  54723> velocity=<-5, -5>
position=< 54711, -43418> velocity=<-5,  4>
position=<-54375,  32914> velocity=< 5, -3>
position=< 21939,  32908> velocity=<-2, -3>
position=<-43477, -10703> velocity=< 4,  1>
position=< 54699,  22012> velocity=<-5, -2>
position=<-54372,  43819> velocity=< 5, -4>
position=< 32841, -21609> velocity=<-3,  2>
position=<-32533,  43816> velocity=< 3, -4>
position=<-54399,  54722> velocity=< 5, -5>
position=<-21649, -21613> velocity=< 2,  2>
position=< 43767,  11107> velocity=<-4, -1>
position=<-21636,  11106> velocity=< 2, -1>
position=< 21964, -10705> velocity=<-2,  1>
position=< 54663, -10712> velocity=<-5,  1>
position=< 32865,  43818> velocity=<-3, -4>
position=< 21992,  22009> velocity=<-2, -2>
position=< 32881, -54331> velocity=<-3,  5>
position=<-32553, -32516> velocity=< 3,  3>
position=<-54381,  54723> velocity=< 5, -5>
position=<-32572,  32908> velocity=< 3, -3>
position=< 43764, -54327> velocity=<-4,  5>
position=<-43482, -32519> velocity=< 4,  3>
position=<-43494, -54325> velocity=< 4,  5>
position=< 11060, -21612> velocity=<-1,  2>
position=<-54350, -10707> velocity=< 5,  1>
position=<-32568,  54719> velocity=< 3, -5>
position=<-43458,  11104> velocity=< 4, -1>
position=<-32548, -54328> velocity=< 3,  5>
position=<-32589,  54725> velocity=< 3, -5>
position=< 43786,  32911> velocity=<-4, -3>
position=< 21940, -32521> velocity=<-2,  3>
position=< 32860, -54323> velocity=<-3,  5>
position=<-54363, -21610> velocity=< 5,  2>
position=< 32846,  54720> velocity=<-3, -5>
position=<-54396, -32517> velocity=< 5,  3>
position=<-32560,  22006> velocity=< 3, -2>
position=<-32549, -10706> velocity=< 3,  1>
position=< 54711, -21613> velocity=<-5,  2>
position=<-21676, -10704> velocity=< 2,  1>
position=< 32878,  11099> velocity=<-3, -1>
position=< 32886, -10710> velocity=<-3,  1>
position=<-21628,  54719> velocity=< 2, -5>
position=< 43802,  54723> velocity=<-4, -5>
position=<-43485, -21608> velocity=< 4,  2>
position=<-10719, -10703> velocity=< 1,  1>
position=< 11043, -21617> velocity=<-1,  2>
position=<-54343, -54328> velocity=< 5,  5>
position=<-10746, -21614> velocity=< 1,  2>
position=<-54399, -54326> velocity=< 5,  5>
position=< 43794,  32913> velocity=<-4, -3>
position=< 21984, -32519> velocity=<-2,  3>
position=< 43770,  43813> velocity=<-4, -4>
position=< 32878, -21616> velocity=<-3,  2>
position=< 21965, -43424> velocity=<-2,  4>
position=<-10746, -32519> velocity=< 1,  3>
position=< 11088, -54328> velocity=<-1,  5>
position=<-32541, -10710> velocity=< 3,  1>
position=< 32873, -10703> velocity=<-3,  1>
position=< 32846, -10706> velocity=<-3,  1>
position=< 11087,  32916> velocity=<-1, -3>
position=< 54667, -21610> velocity=<-5,  2>
position=< 21984, -10707> velocity=<-2,  1>
position=< 11065, -10708> velocity=<-1,  1>
position=< 54708, -54332> velocity=<-5,  5>
position=<-54362, -43418> velocity=< 5,  4>
position=<-32549,  11106> velocity=< 3, -1>
position=< 54684, -10705> velocity=<-5,  1>
position=<-54347, -54331> velocity=< 5,  5>
position=< 43765, -21613> velocity=<-4,  2>
position=< 11082,  54725> velocity=<-1, -5>
position=< 11050, -10703> velocity=<-1,  1>
position=< 21948, -32514> velocity=<-2,  3>
position=<-21648, -32519> velocity=< 2,  3>
position=<-43474,  54727> velocity=< 4, -5>
position=<-54343, -43418> velocity=< 5,  4>
position=< 11031, -43425> velocity=<-1,  4>
position=< 11076,  32909> velocity=<-1, -3>
position=< 11059, -54325> velocity=<-1,  5>
position=<-43493, -54327> velocity=< 4,  5>
position=< 54694, -32518> velocity=<-5,  3>
position=<-54391, -54324> velocity=< 5,  5>
position=<-21660, -32521> velocity=< 2,  3>
position=< 54699, -10706> velocity=<-5,  1>
position=<-54383,  11106> velocity=< 5, -1>
position=<-10755,  54723> velocity=< 1, -5>
position=<-10720, -21608> velocity=< 1,  2>
position=<-21631, -54332> velocity=< 2,  5>
position=<-54378,  32910> velocity=< 5, -3>
position=<-10759, -10712> velocity=< 1,  1>
position=< 43786, -32519> velocity=<-4,  3>
position=<-32570,  54718> velocity=< 3, -5>
position=<-32540, -43423> velocity=< 3,  4>
position=<-21632, -21616> velocity=< 2,  2>
position=<-10718, -32522> velocity=< 1,  3>
position=< 11060,  11103> velocity=<-1, -1>
position=<-43446, -43427> velocity=< 4,  4>
position=<-21660,  43820> velocity=< 2, -4>
position=< 43773, -43422> velocity=<-4,  4>
position=<-10735,  22007> velocity=< 1, -2>
position=< 32846,  43820> velocity=<-3, -4>
position=<-10779,  54720> velocity=< 1, -5>
position=<-54394, -43423> velocity=< 5,  4>
position=<-10747,  32917> velocity=< 1, -3>
position=<-10755,  32917> velocity=< 1, -3>
position=< 54680, -32515> velocity=<-5,  3>
position=< 11079,  32916> velocity=<-1, -3>
position=< 43803,  54727> velocity=<-4, -5>
position=< 11082,  32915> velocity=<-1, -3>
position=< 54710,  32908> velocity=<-5, -3>
position=< 54652, -10711> velocity=<-5,  1>
position=<-10750,  22012> velocity=< 1, -2>
position=< 43758,  32909> velocity=<-4, -3>
position=<-10759,  54721> velocity=< 1, -5>
position=<-32541,  11105> velocity=< 3, -1>
position=<-54349,  54724> velocity=< 5, -5>
position=<-43442, -54331> velocity=< 4,  5>
position=<-32577, -32519> velocity=< 3,  3>
position=<-54399, -21611> velocity=< 5,  2>
position=< 21952, -21617> velocity=<-2,  2>
position=< 11087, -10710> velocity=<-1,  1>
position=< 21994, -54323> velocity=<-2,  5>
position=< 11076, -21614> velocity=<-1,  2>
position=< 11064, -32515> velocity=<-1,  3>
position=<-43476,  11107> velocity=< 4, -1>
position=< 32869, -32514> velocity=<-3,  3>
position=< 11036,  32911> velocity=<-1, -3>
position=<-32541,  43819> velocity=< 3, -4>
position=<-10767,  43815> velocity=< 1, -4>
position=< 43746,  22008> velocity=<-4, -2>
position=<-10775,  11103> velocity=< 1, -1>
position=<-32585, -32521> velocity=< 3,  3>
position=<-54359,  32916> velocity=< 5, -3>
position=< 54652,  11099> velocity=<-5, -1>
position=<-32549, -54323> velocity=< 3,  5>
position=< 43775, -10703> velocity=<-4,  1>
position=< 32877, -54326> velocity=<-3,  5>
position=< 11036, -43422> velocity=<-1,  4>
position=< 54702,  22010> velocity=<-5, -2>
position=< 32867,  43816> velocity=<-3, -4>
position=< 43772,  22007> velocity=<-4, -2>
position=<-10742,  54719> velocity=< 1, -5>
position=< 54696, -10712> velocity=<-5,  1>
position=< 11071, -21613> velocity=<-1,  2>
position=< 43797,  54720> velocity=<-4, -5>
position=< 21965, -32514> velocity=<-2,  3>
position=< 54691,  54725> velocity=<-5, -5>
position=< 21965,  22010> velocity=<-2, -2>
position=<-10723,  43821> velocity=< 1, -4>
position=< 54672,  43814> velocity=<-5, -4>
position=< 43775,  54726> velocity=<-4, -5>
position=<-54351, -10707> velocity=< 5,  1>
position=< 21976, -10703> velocity=<-2,  1>
position=<-43474, -43424> velocity=< 4,  4>
position=<-43489,  11101> velocity=< 4, -1>
position=<-10755, -32513> velocity=< 1,  3>
position=<-54362,  32916> velocity=< 5, -3>
position=<-21655, -43424> velocity=< 2,  4>
position=< 43791, -32513> velocity=<-4,  3>
position=< 21960,  22009> velocity=<-2, -2>
position=<-54351,  11102> velocity=< 5, -1>
position=< 54707, -32514> velocity=<-5,  3>
position=< 21985, -32517> velocity=<-2,  3>
position=< 32862,  11100> velocity=<-3, -1>
position=< 32874,  32914> velocity=<-3, -3>
position=<-21636, -54332> velocity=< 2,  5>
position=< 21961,  54720> velocity=<-2, -5>
position=<-54394, -43423> velocity=< 5,  4>
position=< 21948, -21616> velocity=<-2,  2>
position=<-32584, -21614> velocity=< 3,  2>
position=< 32886,  22008> velocity=<-3, -2>
position=<-21627, -32522> velocity=< 2,  3>
position=<-43460, -32517> velocity=< 4,  3>
position=< 54696, -32519> velocity=<-5,  3>
position=<-32560, -32522> velocity=< 3,  3>
position=<-32578,  54718> velocity=< 3, -5>
position=<-54399,  54723> velocity=< 5, -5>
position=<-43470,  22003> velocity=< 4, -2>
position=< 11036,  11104> velocity=<-1, -1>
position=<-10760, -43427> velocity=< 1,  4>
position=<-54387, -21611> velocity=< 5,  2>
position=<-32530, -21608> velocity=< 3,  2>
position=< 43799,  32908> velocity=<-4, -3>
position=<-10763, -32513> velocity=< 1,  3>
position=<-54378,  43813> velocity=< 5, -4>
position=<-54394, -54325> velocity=< 5,  5>
position=< 43796, -21611> velocity=<-4,  2>
position=< 54675,  54725> velocity=<-5, -5>
position=<-32589, -32513> velocity=< 3,  3>
position=<-21684, -54330> velocity=< 2,  5>
position=< 43780, -10708> velocity=<-4,  1>
position=<-43449, -54329> velocity=< 4,  5>
position=<-54343,  43819> velocity=< 5, -4>
position=< 32898,  54722> velocity=<-3, -5>
position=< 21976, -21612> velocity=<-2,  2>
position=< 54668,  54718> velocity=<-5, -5>
position=< 11044, -10712> velocity=<-1,  1>
position=< 11063, -32522> velocity=<-1,  3>
position=< 11071, -43423> velocity=<-1,  4>
position=< 54704,  54727> velocity=<-5, -5>
position=< 54651,  11101> velocity=<-5, -1>
position=< 54656, -43418> velocity=<-5,  4>
position=< 54696, -10711> velocity=<-5,  1>
position=< 32899,  54718> velocity=<-3, -5>
position=<-21672, -54328> velocity=< 2,  5>
position=< 54684,  32910> velocity=<-5, -3>
position=<-54367,  11099> velocity=< 5, -1>
position=< 11075,  11102> velocity=<-1, -1>
position=<-32554,  22008> velocity=< 3, -2>
position=< 54712, -10703> velocity=<-5,  1>
position=<-10743,  11100> velocity=< 1, -1>
position=< 11076,  43820> velocity=<-1, -4>
position=< 32881, -43427> velocity=<-3,  4>
position=<-32577, -43426> velocity=< 3,  4>
position=< 32870,  54727> velocity=<-3, -5>
position=<-32536,  43822> velocity=< 3, -4>
position=<-32573,  22012> velocity=< 3, -2>
position=< 32873, -10711> velocity=<-3,  1>
position=<-21655, -54325> velocity=< 2,  5>
position=< 43791,  43819> velocity=<-4, -4>
position=<-54388,  22003> velocity=< 5, -2>
position=< 54712,  32908> velocity=<-5, -3>
position=<-43491, -32517> velocity=< 4,  3>
position=< 21960,  11106> velocity=<-2, -1>
position=< 32886,  11100> velocity=<-3, -1>
position=<-10767,  43819> velocity=< 1, -4>
position=<-10739, -21615> velocity=< 1,  2>
position=<-21624,  54722> velocity=< 2, -5>
position=< 32841,  54725> velocity=<-3, -5>
position=<-32549, -43421> velocity=< 3,  4>
position=< 11079, -21617> velocity=<-1,  2>
position=<-54378,  43813> velocity=< 5, -4>
position=< 32889, -21614> velocity=<-3,  2>
position=< 11064,  32915> velocity=<-1, -3>
position=<-54349,  32914> velocity=< 5, -3>
position=< 43779, -10706> velocity=<-4,  1>
position=<-54387,  22009> velocity=< 5, -2>
position=< 21981, -32520> velocity=<-2,  3>
position=<-43493, -32517> velocity=< 4,  3>
position=<-43470,  11101> velocity=< 4, -1>
position=<-32549, -32514> velocity=< 3,  3>
position=< 21952,  32916> velocity=<-2, -3>
position=< 54675, -21613> velocity=<-5,  2>
position=<-54367, -21608> velocity=< 5,  2>
position=<-10720,  32912> velocity=< 1, -3>
position=<-54399, -54327> velocity=< 5,  5>
position=< 11050, -21613> velocity=<-1,  2>
position=< 32886, -43420> velocity=<-3,  4>
position=< 21962, -10708> velocity=<-2,  1>
position=<-32584,  11103> velocity=< 3, -1>
position=< 43748,  32913> velocity=<-4, -3>
position=<-32565, -10703> velocity=< 3,  1>
position=< 21984, -32520> velocity=<-2,  3>
position=<-10768, -32513> velocity=< 1,  3>
position=< 32843,  43813> velocity=<-3, -4>
position=< 21965, -54331> velocity=<-2,  5>
position=<-32581, -10705> velocity=< 3,  1>
position=<-10739,  32910> velocity=< 1, -3>
position=< 54709,  32917> velocity=<-5, -3>
position=<-54378,  22012> velocity=< 5, -2>
position=< 43748,  11098> velocity=<-4, -1>
position=<-21647, -10712> velocity=< 2,  1>
position=< 11058, -10706> velocity=<-1,  1>
position=< 11087, -54323> velocity=<-1,  5>
position=< 32898, -10708> velocity=<-3,  1>
position=<-54387,  54718> velocity=< 5, -5>
position=< 32889,  11104> velocity=<-3, -1>
position=< 43794, -10710> velocity=<-4,  1>
position=<-32569, -32513> velocity=< 3,  3>
position=< 21996,  11098> velocity=<-2, -1>
position=<-21679, -43422> velocity=< 2,  4>
position=<-32553, -43424> velocity=< 3,  4>
position=<-10761, -32522> velocity=< 1,  3>
position=<-54372,  54723> velocity=< 5, -5>
position=< 21953,  11104> velocity=<-2, -1>
position=< 11047,  22003> velocity=<-1, -2>
position=<-21639,  32917> velocity=< 2, -3>
position=<-43444,  32911> velocity=< 4, -3>
position=< 54653,  11103> velocity=<-5, -1>
position=< 43791,  54724> velocity=<-4, -5>
position=<-21672, -21610> velocity=< 2,  2>
position=<-54367,  11106> velocity=< 5, -1>
position=< 32866,  22004> velocity=<-3, -2>
position=< 32874,  43819> velocity=<-3, -4>
position=<-32533,  43820> velocity=< 3, -4>
position=<-21648,  22010> velocity=< 2, -2>
position=<-43493, -21616> velocity=< 4,  2>
position=< 43763,  43813> velocity=<-4, -4>
position=<-54359,  54720> velocity=< 5, -5>
position=< 43794,  43814> velocity=<-4, -4>
position=< 54653, -54327> velocity=<-5,  5>
position=<-10739,  11099> velocity=< 1, -1>
position=<-32577,  22008> velocity=< 3, -2>
position=< 54696, -32518> velocity=<-5,  3>
position=< 11076,  54722> velocity=<-1, -5>
position=<-10776, -32522> velocity=< 1,  3>
position=<-54358,  32912> velocity=< 5, -3>
position=< 11087, -10712> velocity=<-1,  1>
position=< 43758, -21613> velocity=<-4,  2>
position=< 54693, -54328> velocity=<-5,  5>
position=<-21628,  43820> velocity=< 2, -4>
position=<-54343,  54725> velocity=< 5, -5>
position=< 21940, -54331> velocity=<-2,  5>
position=<-43492, -43427> velocity=< 4,  4>
position=< 21986,  43816> velocity=<-2, -4>
position=< 11060, -10704> velocity=<-1,  1>
position=< 54696,  11106> velocity=<-5, -1>
position=< 32876,  32913> velocity=<-3, -3>
position=< 43787,  22007> velocity=<-4, -2>
position=<-32586,  22008> velocity=< 3, -2>
position=< 32899, -10703> velocity=<-3,  1>"""

val input_10_test =
    """position=< 9,  1> velocity=< 0,  2>
position=< 7,  0> velocity=<-1,  0>
position=< 3, -2> velocity=<-1,  1>
position=< 6, 10> velocity=<-2, -1>
position=< 2, -4> velocity=< 2,  2>
position=<-6, 10> velocity=< 2, -2>
position=< 1,  8> velocity=< 1, -1>
position=< 1,  7> velocity=< 1,  0>
position=<-3, 11> velocity=< 1, -2>
position=< 7,  6> velocity=<-1, -1>
position=<-2,  3> velocity=< 1,  0>
position=<-4,  3> velocity=< 2,  0>
position=<10, -3> velocity=<-1,  1>
position=< 5, 11> velocity=< 1, -2>
position=< 4,  7> velocity=< 0, -1>
position=< 8, -2> velocity=< 0,  1>
position=<15,  0> velocity=<-2,  0>
position=< 1,  6> velocity=< 1,  0>
position=< 8,  9> velocity=< 0, -1>
position=< 3,  3> velocity=<-1,  1>
position=< 0,  5> velocity=< 0, -1>
position=<-2,  2> velocity=< 2,  0>
position=< 5, -2> velocity=< 1,  2>
position=< 1,  4> velocity=< 2,  1>
position=<-2,  7> velocity=< 2, -2>
position=< 3,  6> velocity=<-1, -1>
position=< 5,  0> velocity=< 1,  0>
position=<-6,  0> velocity=< 2,  0>
position=< 5,  9> velocity=< 1, -2>
position=<14,  7> velocity=<-2,  0>
position=<-3,  6> velocity=< 2, -1>"""