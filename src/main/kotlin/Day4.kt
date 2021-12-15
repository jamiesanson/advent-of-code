fun Day4Part1(inputs: List<String>): Int {
    val bingoCalls = inputs.first().split(",").map { it.toInt() }

    data class Board(
        val tiles: List<List<Int>>
    )

    val boards = inputs
        .drop(1)
        .filter { it.isNotEmpty() }
        .windowed(5, step = 5)
        .map { boardLines ->
            Board(boardLines.map {
                it.split(" ").filterNot { it.isEmpty() }.map { it.trim().toInt() }
            })
        }

    for (i in bingoCalls.indices) {
        val calls = bingoCalls.subList(0, i)

        for (board in boards) {
            // Check for 5 in a rows
            val hasWinningRow = board.tiles.any { it.all { calls.contains(it) } }

            // Check for 5 in columns
            val hasWinningColumn = (0 until board.tiles.size)
                .map { index -> board.tiles.map { it[index] } }
                .any { it.all { calls.contains(it) } }

            if (hasWinningColumn || hasWinningRow) {
                println("Winning board: $board")
                return board.tiles.flatten().filterNot { calls.contains(it) }.sum() * calls.last()
            }
        }
    }

    return -1
}

fun Day4Part2(inputs: List<String>): Int {
    val bingoCalls = inputs.first().split(",").map { it.toInt() }

    data class Board(
        val tiles: List<List<Int>>
    )

    val boards = inputs
        .asSequence()
        .drop(1)
        .filter { it.isNotEmpty() }
        .windowed(5, step = 5)
        .map { boardLines ->
            Board(boardLines.map {
                it.split(" ").filterNot { it.isEmpty() }.map { it.trim().toInt() }
            })
        }
        .toMutableList()

    for (i in bingoCalls.indices) {
        val calls = bingoCalls.subList(0, i)

        val boardsThatWon = boards.filter { board ->
            // Check for 5 in a rows
            val hasWinningRow = board.tiles.any { it.all { calls.contains(it) } }

            // Check for 5 in columns
            val hasWinningColumn = (0 until board.tiles.size)
                .map { index -> board.tiles.map { it[index] } }
                .any { it.all { calls.contains(it) } }

            if (hasWinningColumn || hasWinningRow) {
                println("Winning board: $board")
                true
            } else {
                false
            }
        }

        boards.removeAll(boardsThatWon)

        if (boards.size == 0) {
            return boardsThatWon.first().tiles.flatten().filterNot { calls.contains(it) }.sum() * calls.last()
        }
    }

    return -1
}