package mmxxiiii

import io.kotest.matchers.shouldBe
import mmxxiiii.input.input

private data class Rule(val first: Int, val second: Int) {
    fun allows(list: List<Int>): Boolean {
        return list.indexOf(first) < list.indexOf(second)
    }
}

private fun List<String>.toRules(): List<Rule> =
    takeWhile { it.isNotBlank() }
        .map { it.split("|") }
        .map { (first, second) -> Rule(first.toInt(), second.toInt()) }

private fun List<String>.toUpdates(): List<List<Int>> =
    takeLastWhile { it.isNotBlank() }
        .map { line -> line.split(",").map(String::toInt) }

private fun List<Int>.containsParts(rule: Rule): Boolean {
    return contains(rule.first) && contains(rule.second)
}

private fun List<List<Int>>.correctlyOrdered(rules: List<Rule>): List<List<Int>> =
    filter { update ->
        rules.filter { update.containsParts(it) }.all { rule -> rule.allows(update) }
    }

private fun Day5Part1(lines: List<String>): Int {
    val rules = lines.toRules()
    val updates = lines.toUpdates()

    val correctlyOrdered = updates.correctlyOrdered(rules)

    return correctlyOrdered.sumOf { update -> update[update.size / 2] }
}

private fun Day5Part2(lines: List<String>): Int {
    val rules = lines.toRules()
    val updates = lines.toUpdates()

    val correctlyOrdered = updates.correctlyOrdered(rules)
    val incorrectlyOrdered = updates.filterNot { it in correctlyOrdered }

    val newlyOrdered = incorrectlyOrdered.map { update ->
        val applicable = rules.filter { update.containsParts(it) }

        val newUpdate = update.toMutableList()

        // Keep swapping elements around until the rules are all satisfied
        while (!applicable.all { it.allows(newUpdate) }) {
            for (rule in applicable) {
                if (!rule.allows(newUpdate)) {
                    val first = newUpdate.indexOf(rule.first)
                    val second = newUpdate.indexOf(rule.second)

                    newUpdate[first] = rule.second
                    newUpdate[second] = rule.first
                }
            }
        }

        newUpdate
    }

    return newlyOrdered.sumOf { update -> update[update.size / 2] }
}

fun main() {
    val input = input(day = 5)

    Day5Part1(input.sample()) shouldBe 143
    Day5Part1(input.main()) shouldBe 6260

    Day5Part2(input.sample()) shouldBe 123
    Day5Part2(input.main()) shouldBe 123
}