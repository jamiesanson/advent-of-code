import day1.Day1
import day1.Day1Part2
import java.io.File

fun main(args: Array<String>) {

    println(File("").absolutePath)
    val input = File("input").readLines().joinToString(separator = "\n")

    println("Day 1 pt 2: ${Day1Part2(input)}")
}