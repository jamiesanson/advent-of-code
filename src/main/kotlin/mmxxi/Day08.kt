package mmxxi

fun Day8Part1(input: List<String>): Int {
    return input
        .map { it.split("|").map { it.trim() } }
        .map { (_, output) ->
            output.split(" ")
                .count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
        }
        .sum()
}

private fun List<String>.sortElements(): List<String> = map {
    it.toSortedSet().joinToString("")
}

private infix fun String.containsAll(that: String): Boolean =
    this.toSet().containsAll(that.toSet())

fun Day8Part2(input: List<String>): Int {

    fun parseInput(digits: List<String>): Map<String, Int> {
        val digitMap = mutableMapOf<Int, String>()

        digitMap[1] = digits.first { it.length == 2 }
        digitMap[4] = digits.first { it.length == 4 }
        digitMap[7] = digits.first { it.length == 3 }
        digitMap[8] = digits.first { it.length == 7 }

        digitMap[3] = digits
            .filter { it.length == 5 }
            .first { it containsAll digitMap[1]!! }

        // 9 is length 6 and overlaps 3
        digitMap[9] = digits
            .filter { it.length == 6 }
            .first { it containsAll digitMap[3]!! }

        // 0 is length 6, overlaps 1 and 7, and is not 9
        digitMap[0] = digits
            .filter { it.length == 6 }
            .filter { it containsAll digitMap[1]!! && it containsAll digitMap[7]!! }
            .first { it != digitMap[9] }

        // 6 is length 6 and is not 0 or 9
        digitMap[6] = digits
            .filter { it.length == 6 }
            .first { it != digitMap[0] && it != digitMap[9] }

        // 5 is length 5 and is overlapped by 6
        digitMap[5] = digits
            .filter { it.length == 5 }
            .first { digitMap[6]!! containsAll it }

        // 2 is length 5 and is not 3 or 5
        digitMap[2] = digits
            .filter { it.length == 5 }
            .first { it != digitMap[3] && it != digitMap[5] }

        return digitMap.map { (key, value) -> value to key }.toMap()
    }

    return input
        .map { it.split("|") }
        .sumOf { (digits, values) ->
            val row = digits.trim().split(" ").sortElements()
            val targets = values.trim().split(" ").sortElements()
            val digitValues = parseInput(row)

            targets.map { digitValues[it] }.joinToString("").toInt()
        }
}