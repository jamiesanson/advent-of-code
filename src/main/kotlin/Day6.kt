fun Day6Part1(inputs: List<String>): Int {
    val generations = 80

    val fish = inputs.first().split(",").map { it.toInt() }.toMutableList()

    for (day in 0 until generations) {
        val newFish = mutableListOf<Int>()
        fish.replaceAll {
            if (it > 0) it.dec() else 6.also { newFish.add(8) }
        }

        fish.addAll(newFish)
    }

    return fish.size
}

fun Day6Part2(inputs: List<String>): Long {
    return Day06(inputs.first()).solvePart2()
}

class Day06(input: String) {

    private val fishiesPerDay: LongArray = parseInput(input)

    fun solvePart1(): Long =
        simulateDays(80)

    fun solvePart2(): Long =
        simulateDays(256)

    private fun simulateDays(days: Int): Long {
        repeat(days) {
            fishiesPerDay.rotateLeftInPlace()
            fishiesPerDay[6] += fishiesPerDay[8]
        }
        return fishiesPerDay.sum()
    }

    private fun LongArray.rotateLeftInPlace() {
        val leftMost = first()
        this.copyInto(this, startIndex = 1)
        this[this.lastIndex] = leftMost
    }

    private fun parseInput(input: String): LongArray =
        LongArray(9).apply {
            input.split(",").map { it.toInt() }.forEach { this[it] += 1L }
        }
}