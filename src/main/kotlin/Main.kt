import java.io.File

fun main(args: Array<String>) {
    val input = File("input").readLines()

    println("Day 6 pt 2: ${Day6Part2(input)}")
}