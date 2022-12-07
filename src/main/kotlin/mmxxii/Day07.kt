package mmxxii

private sealed class Node {
    abstract val size: Int

    data class File(
        override val size: Int
    ) : Node()

    data class Dir(
        val parent: Dir? = null
    ) : Node() {

        val children = mutableMapOf<String, Node>()

        override val size: Int by lazy { children.values.sumOf { it.size } }
    }
}

private fun List<String>.parseToDir(): Node.Dir {
    val rootDir = Node.Dir()
    var currentDir = rootDir

    for (line in drop(1)) {
        // Deal with command lines
        if (line.startsWith("$")) {
            val command = line.removePrefix("$ ").split(" ")
            when (command.first()) {
                "ls" -> Unit // Nothing to do, skip this
                "cd" -> {
                    currentDir = if (command[1] == "..") {
                        currentDir.parent ?: throw IllegalStateException("No parent")
                    } else {
                        currentDir.children[command[1]] as Node.Dir
                    }
                }
            }
        }
        // Deal with readout lines
        else {
            val (first, second) = line.split(" ")
            if (first == "dir") {
                currentDir.children[second] = Node.Dir(parent = currentDir)
            } else {
                currentDir.children[second] = Node.File(size = first.toInt())
            }
        }
    }

    return rootDir
}

fun Day07Part1(input: List<String>): Int {
    val rootDir = input.parseToDir()

    fun Node.Dir.findSmallDirs(): List<Node.Dir> {
        return children.values
            .filterIsInstance<Node.Dir>()
            .flatMap { it.findSmallDirs() } + listOfNotNull(this.takeIf { it.size < 100000 })
    }

    return rootDir.findSmallDirs().sumOf { it.size }
}

fun Day07Part2(input: List<String>): Int {
    val rootDir = input.parseToDir()

    fun Node.Dir.flatten(): List<Node.Dir> {
        return children.values
            .filterIsInstance<Node.Dir>()
            .flatMap { it.flatten() } + this
    }

    val diskSize = 70000000
    val updateSize = 30000000

    val fsSize = rootDir.size
    val freeSpace = diskSize - fsSize

    return rootDir
        .flatten()
        .map { it.size }
        .sorted()
        .first { freeSpace + it > updateSize }
}