import java.io.File

fun main(args: Array<String>) {
    val input = File("input").readLines()

    println("Day 5 pt 2: ${Day5Part2(input)}")
}