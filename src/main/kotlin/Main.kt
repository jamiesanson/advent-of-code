import java.io.File

fun main(args: Array<String>) {
    val input = File("input").readLines()

    println("Day 3 pt 2: ${Day3Part2(input)}")
}