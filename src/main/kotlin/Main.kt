import java.io.File

fun main(args: Array<String>) {
    val input = File("input").readLines()

    println("Day 4 pt 2: ${Day4Part2(input)}")
}