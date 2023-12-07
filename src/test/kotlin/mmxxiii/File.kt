package mmxxiii

import java.io.File

fun Any.sampleData(): List<String> = relativeFile("sample").readLines()

fun Any.inputData(): List<String> = relativeFile("input").readLines()

private fun Any.relativeFile(name: String): File {
    val packageComponents = this::class.java.`package`.name.replace(".", "/")
    return File("src/test/kotlin/$packageComponents/$name")
}