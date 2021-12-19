fun Day12Part1(input: List<String>): Int {
    data class Cave(val name: String) {
        val isBig = name.all { it.isUpperCase() }
        val isEnd = name == "end"
    }

    val allCaves = input
        .map { row -> row.split("-").map { Cave(it) } }
        .flatMap { (a, b) ->
            listOf(a to b, b to a)
        }.groupBy({ it.first }, { it.second })

    fun walkRoute(path: List<Cave>): List<List<Cave>> {
        if (path.last().isEnd) return listOf(path)

        return allCaves[path.last()]!!
            .filterNot { !it.isBig && path.contains(it) }
            .flatMap { walkRoute(path + it) }
    }

    return walkRoute(listOf(Cave("start"))).size
}

fun Day12Part2(input: List<String>): Int {
    data class Cave(val name: String) {
        val isBig = name.all { it.isUpperCase() }
        val isStart = name == "start"
        val isEnd = name == "end"
    }

    val allCaves = input
        .map { row -> row.split("-").map { Cave(it) } }
        .flatMap { (a, b) ->
            listOf(a to b, b to a)
        }.groupBy({ it.first }, { it.second })

    fun walkRoute(path: List<Cave>): List<List<Cave>> {
        if (path.last().isEnd) return listOf(path)

        return allCaves[path.last()]!!
            .filter { cave ->
                when {
                    cave.isStart -> false
                    cave.isBig -> true
                    cave !in path -> true
                    else -> path
                        .filterNot { it.isBig }
                        .groupBy { it }
                        .none { it.value.size == 2 }
                }
            }
            .flatMap { walkRoute(path + it) }
    }

    return walkRoute(listOf(Cave("start"))).size
}