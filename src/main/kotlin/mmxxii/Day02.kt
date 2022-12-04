package mmxxii

fun Day02Part1(input: List<String>): Int {
    val roundInts = input.map {
        val split = it.split(' ')
        (split[0].first().code - 64) to (split[1].first().code - 23 - 64)
    }

    return roundInts.fold(0) { acc, (opp, me) ->
        val roundScore = me + when (me - opp) {
            1, -2 -> 6 // win
            0 -> 3 // draw
            else -> 0 // this is loss
        }

        acc + roundScore
    }
}

fun Day02Part2(input: List<String>): Int {
    val roundInts = input.map {
        val split = it.split(' ')
        (split[0].first().code - 64) to (split[1].first().code - 23 - 64)
    }

    return roundInts.fold(0) { acc, (opp, outcome) ->
        val outcomeScore = (outcome - 1) * 3
        val choiceScore = when (outcome) {
            1 -> if (opp > 1) opp - 1 else 3
            3 -> if (opp < 3) opp + 1 else 1
            else -> opp
        }
        acc + outcomeScore + choiceScore
    }
}