package mmxxii

private fun List<String>.parseStacks(): List<ArrayDeque<Char>> {
    val stacks = mutableListOf<ArrayDeque<Char>>()
    this
        .takeWhile { it.isNotBlank() }
        .dropLast(1)
        .map { line ->
            line
                .replace("    ", "[-]")
                .filterNot { it == '[' || it == ']' || it.isWhitespace() }
                .toCharArray()
        }
        .forEach { s ->
            s.forEachIndexed { index, c ->
                val queue = stacks.getOrNull(index)
                    ?: ArrayDeque<Char>().also { stacks.add(index, it) }

                if (c != '-') queue.add(c)
            }
        }

    return stacks
}


private data class Move(val count: Int, val source: Int, val dest: Int)

private fun List<String>.parseMoves(): List<Move> {
    return takeLastWhile { it.isNotBlank() }
        .map { line -> line.filter { it.isDigit() || it.isWhitespace() } }
        .map { line ->
            val split = line.split(" ").filter { it.isNotBlank() }
            Move(
                count = split[0].toInt(),
                source = split[1].toInt() - 1,
                dest = split[2].toInt() - 1
            )
        }
}

fun Day05Part1(input: List<String>): String {
    val stacks = input.parseStacks()
    val moves = input.parseMoves()

    moves.forEach { (count, source, dest) ->
        repeat(count) {
            stacks[dest].addFirst(stacks[source].removeFirst())
        }
    }

    return stacks.map { it.first() }.joinToString(separator = "")
}

fun Day05Part2(input: List<String>): String {
    val stacks = input.parseStacks()
    val moves = input.parseMoves()

    moves.forEach { (count, source, dest) ->
        val itemsToPush = (1..count).map { stacks[source].removeFirst() }

        itemsToPush.reversed().forEach {
            stacks[dest].addFirst(it)
        }
    }

    return stacks.map { it.first() }.joinToString(separator = "")
}