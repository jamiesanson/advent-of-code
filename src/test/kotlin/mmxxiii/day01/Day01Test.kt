package mmxxiii.day01

import io.kotest.matchers.shouldBe
import mmxxiii.Day01Part1
import mmxxiii.inputData
import mmxxiii.sampleData
import kotlin.test.Test

class Day01Test {

    @Test
    fun sample() {
        Day01Part1(sampleData()) shouldBe 142
    }

    @Test
    fun full() {
        Day01Part1(inputData()) shouldBe 55607
    }
}