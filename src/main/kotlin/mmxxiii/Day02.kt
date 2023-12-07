package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.input.Input

data class Reveal(
    val green: Int,
    val red: Int,
    val blue: Int,
)

data class Game(
    val id: Int,
    val reveals: List<Reveal>,
)

fun List<String>.parseGames(): List<Game> = map { line ->
    Game(
        id = line.takeWhile { it != ':' }.filter { it.isDigit() }.toInt(),
        reveals = line
            .dropWhile { it != ':' }
            .drop(1)
            .split(";")
            .map { draw ->
                val counts = draw
                    .split(",")
                    .map { it.trim() }
                    .associate {
                        val (count, colour) = it.split(' ')
                        colour to count.toInt()
                    }

                Reveal(
                    red = counts["red"] ?: 0,
                    green = counts["green"] ?: 0,
                    blue = counts["blue"] ?: 0,
                )
            }
    )
}

fun Day02Part1(input: List<String>): Int {
    val games = input.parseGames()

    return games
        .filterNot { game ->
            game.reveals.any {
                it.red > 12 || it.green > 13 || it.blue > 14
            }
        }.sumOf { it.id }
}

fun Day02Part2(input: List<String>): Int {
    val games = input.parseGames()

    return games.sumOf { game ->
        val maxRed = game.reveals.maxOfOrNull { it.red } ?: 0
        val maxGreen = game.reveals.maxOfOrNull { it.green } ?: 0
        val maxBlue = game.reveals.maxOfOrNull { it.blue } ?: 0

        maxRed * maxGreen * maxBlue
    }
}

fun main() {
    val input = Input("02")

    Day02Part1(input.sample()) shouldBe 8
    Day02Part1(input.main()) shouldBe 2265

    Day02Part2(input.sample()) shouldBe 2286
    Day02Part2(input.main()) shouldBe 64097
}