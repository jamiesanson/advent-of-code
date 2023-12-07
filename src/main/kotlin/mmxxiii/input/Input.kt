package mmxxiii.input

import java.io.File

class Input(
    private val day: String,
) {
    private fun file(name: String): File = File("src/main/kotlin/mmxxiii/input/day$day/$name")

    fun main(): List<String> {
        return file("input").readLines()
    }

    fun sample(part: Int? = null): List<String> {
        return file(if (part != null) "sample_pt$part" else "sample").readLines()
    }
}