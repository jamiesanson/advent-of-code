package util

import java.io.File

class Input(
    private val day: Int,
    private val year: String,
) {
    private val paddedDay = day.toString().padStart(2, '0')

    private fun file(name: String): File = File("src/main/kotlin/$year/input/day$paddedDay/$name")

    fun main(): List<String> {
        return file("input").readLines()
    }

    fun sample(part: Int? = null): List<String> {
        return file(if (part != null) "sample_pt$part" else "sample").readLines()
    }
}