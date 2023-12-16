package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.Input

private fun String.hash(): Int {
    return fold(0) { acc, char ->
        ((acc + char.code) * 17) % 256
    }
}

fun Day15Part1(input: List<String>): Int {
    val steps = input.first().split(",")

    return steps.sumOf { it.hash() }
}

fun Day15Part2(input: List<String>): Int {
    data class Lens(val label: String, val power: Int)

    val boxes = List(256) { it }
        .associateWith { mutableListOf<Lens>() }
        .toMutableMap()

    val instructions = input.first().split(",")

    instructions.forEach { instruction ->
        if (instruction.contains('-')) {
            val label = instruction.removeSuffix("-")

            boxes.getValue(label.hash()).removeIf { it.label == label }
        } else {
            val (label, power) = instruction.split("=")
            val lens = Lens(label, power.toInt())

            val box = boxes.getValue(label.hash())
            if (box.any { it.label == lens.label }) {
                box.replaceAll {
                    if (it.label == lens.label) lens else it
                }
            } else {
                box.add(lens)
            }
        }
    }

    return boxes
        .flatMap { (boxNumber, value) ->
            value.mapIndexed { index, lens ->
                (boxNumber + 1) * (index + 1) * lens.power
            }
        }
        .sum()
}

fun main() {
    val input = Input("15")

    "HASH".hash() shouldBe 52

    Day15Part1(input.sample()) shouldBe 1320
    Day15Part1(input.main()) shouldBe 508552

    Day15Part2(input.sample()) shouldBe 145
    Day15Part2(input.main()) shouldBe 265462
}