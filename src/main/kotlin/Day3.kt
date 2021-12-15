import kotlin.experimental.and
import kotlin.experimental.inv

fun Day3Part1(input: List<String>): Long {
    val means = (0 until input.first().length)
        .map { charIndex ->
            input.sumOf { it[charIndex].toString().toInt() }.toFloat() / input.size.toFloat()
        }

    val gamma = means.map { if (it > 0.5) '1' else '0' }
        .joinToString(separator = "")
        .toLong(radix = 2)

    println("Gamma: $gamma")

    val epsilon = means.map { if (it < 0.5) '1' else '0' }
        .joinToString(separator = "")
        .toLong(radix = 2)

    println("Epsilon: ${epsilon}")

    return gamma * epsilon
}


fun Day3Part2(input: List<String>): Int {
    val oxyCandidates = input.toMutableList()
    val co2Candidates = input.toMutableList()

    fun meansFor(candidates: List<String>): List<Float> {
        return (0 until input.first().length)
            .map { charIndex ->
                candidates.sumOf { it[charIndex].toString().toInt() }.toFloat() / candidates.size.toFloat()
            }
    }

    for (bitPos in 0 until input.first().length) {
        val mostCommon = if (meansFor(oxyCandidates)[bitPos] >= 0.5) '1' else '0'

        if (oxyCandidates.size > 1) {
            oxyCandidates.removeAll { it[bitPos] != mostCommon }
        }

        val leastCommon = if (meansFor(co2Candidates)[bitPos] >= 0.5) '0' else '1'
        if (co2Candidates.size > 1) {
            co2Candidates.removeAll { it[bitPos] != leastCommon }
        }
    }

    val oxy = oxyCandidates.first().toInt(radix = 2)
    val co2 = co2Candidates.first().toInt(radix = 2)

    println("Oxy: $oxy; CO2: $co2")

    return oxy * co2
}