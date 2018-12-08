val p07 = fun() {
    val input = input_7.lines().map { it.split(" ") }.map { listOf(it[1], it[7]) }

    val allSteps = input.flatten().distinct()
    val completed = mutableListOf<String>()

    fun canStart() = (allSteps - completed).filterNot { s ->
        input.any { it[1] == s && !completed.contains(it[0]) }
    }

    while (completed.size < allSteps.size) {

        val can = canStart().sorted()
        completed.add(can.first())
    }

    completed.joinToString("").print()

    val running = mutableListOf<Pair<String, Int>>()
    completed.clear()
    var clock = 0;
    val numWorkers = 5
    val offset = 60

    fun canStart2() = (allSteps - completed - running.map { it.first }).filterNot { s ->
        input.any { it[1] == s && !completed.contains(it[0]) }
    }

    while (completed.size < allSteps.size) {
        val done = running.filter { it.second == clock }
        completed.addAll(done.map { it.first })
        running.removeAll(done)

        val can = canStart2().sorted().take(numWorkers - running.size)
        running.addAll(can.map { Pair(it, clock + offset + (it[0] - 64).toInt()) })

        running.print()

        clock++
    }

    (clock - 1).print()



}


val input_7_test = """"Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin."""

val input_7 = """Step P must be finished before step F can begin.
Step F must be finished before step M can begin.
Step Q must be finished before step S can begin.
Step K must be finished before step G can begin.
Step W must be finished before step X can begin.
Step V must be finished before step I can begin.
Step S must be finished before step Y can begin.
Step U must be finished before step D can begin.
Step J must be finished before step B can begin.
Step Z must be finished before step C can begin.
Step Y must be finished before step D can begin.
Step X must be finished before step A can begin.
Step E must be finished before step N can begin.
Step M must be finished before step B can begin.
Step N must be finished before step I can begin.
Step I must be finished before step T can begin.
Step H must be finished before step A can begin.
Step A must be finished before step B can begin.
Step O must be finished before step L can begin.
Step T must be finished before step L can begin.
Step D must be finished before step R can begin.
Step G must be finished before step L can begin.
Step C must be finished before step R can begin.
Step R must be finished before step L can begin.
Step L must be finished before step B can begin.
Step O must be finished before step R can begin.
Step Q must be finished before step I can begin.
Step M must be finished before step L can begin.
Step R must be finished before step B can begin.
Step J must be finished before step O can begin.
Step O must be finished before step B can begin.
Step Y must be finished before step L can begin.
Step G must be finished before step R can begin.
Step P must be finished before step Z can begin.
Step K must be finished before step Y can begin.
Step X must be finished before step I can begin.
Step E must be finished before step H can begin.
Step I must be finished before step H can begin.
Step P must be finished before step K can begin.
Step G must be finished before step B can begin.
Step H must be finished before step L can begin.
Step X must be finished before step C can begin.
Step P must be finished before step X can begin.
Step X must be finished before step M can begin.
Step Q must be finished before step H can begin.
Step S must be finished before step Z can begin.
Step C must be finished before step B can begin.
Step N must be finished before step A can begin.
Step M must be finished before step R can begin.
Step X must be finished before step E can begin.
Step P must be finished before step L can begin.
Step H must be finished before step G can begin.
Step E must be finished before step D can begin.
Step D must be finished before step L can begin.
Step W must be finished before step A can begin.
Step S must be finished before step X can begin.
Step V must be finished before step O can begin.
Step H must be finished before step B can begin.
Step T must be finished before step B can begin.
Step Y must be finished before step C can begin.
Step A must be finished before step R can begin.
Step N must be finished before step L can begin.
Step V must be finished before step Z can begin.
Step W must be finished before step V can begin.
Step S must be finished before step M can begin.
Step Z must be finished before step A can begin.
Step W must be finished before step S can begin.
Step Q must be finished before step R can begin.
Step N must be finished before step G can begin.
Step Z must be finished before step L can begin.
Step K must be finished before step O can begin.
Step X must be finished before step R can begin.
Step V must be finished before step H can begin.
Step P must be finished before step R can begin.
Step M must be finished before step A can begin.
Step K must be finished before step L can begin.
Step P must be finished before step M can begin.
Step F must be finished before step N can begin.
Step W must be finished before step H can begin.
Step K must be finished before step B can begin.
Step H must be finished before step C can begin.
Step X must be finished before step H can begin.
Step V must be finished before step U can begin.
Step S must be finished before step H can begin.
Step J must be finished before step X can begin.
Step S must be finished before step N can begin.
Step V must be finished before step A can begin.
Step H must be finished before step O can begin.
Step Y must be finished before step O can begin.
Step H must be finished before step R can begin.
Step X must be finished before step T can begin.
Step J must be finished before step H can begin.
Step G must be finished before step C can begin.
Step E must be finished before step R can begin.
Step W must be finished before step J can begin.
Step F must be finished before step E can begin.
Step P must be finished before step I can begin.
Step F must be finished before step T can begin.
Step J must be finished before step L can begin.
Step U must be finished before step Z can begin.
Step Q must be finished before step D can begin."""