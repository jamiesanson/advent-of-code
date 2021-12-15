import day2.Day2Part2
import java.io.File

fun main(args: Array<String>) {
    val input = File("input").readLines()

    println("Day 2 pt 1: ${Day2Part2(input)}")
}