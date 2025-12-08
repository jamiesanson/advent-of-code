package mmxxv

import io.kotest.matchers.shouldBe
import mmxxv.input.input
import kotlin.math.pow
import kotlin.math.sqrt

private data class Vector3D(val x: Double, val y: Double, val z: Double)

private fun Vector3D.distanceTo(other: Vector3D): Double {
    return sqrt((x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2))
}

private fun List<String>.toVecPairs(): List<Pair<Vector3D, Vector3D>> {
    val vectors = map {
        val (x, y, z) = it.split(",")
        Vector3D(x.toDouble(), y.toDouble(), z.toDouble())
    }

    return vectors
        .flatMap { vec ->
            vectors
                .filterNot { it == vec }
                .map {
                    Triple(vec, it, it.distanceTo(vec))
                }
        }
        .distinctBy { it.third }
        .sortedBy { it.third }
        .map { it.first to it.second }
}

private fun MutableList<MutableList<Vector3D>>.connect(
    a: Vector3D,
    b: Vector3D
): Boolean {
    val aCircuit = find { it.contains(a) }
    val bCircuit = find { it.contains(b) }

    when {
        aCircuit != null && aCircuit == bCircuit -> return false
        aCircuit != null && bCircuit != null -> {
            aCircuit.addAll(bCircuit)
            remove(bCircuit)
        }

        aCircuit != null -> {
            aCircuit.add(b)
        }

        bCircuit != null -> {
            bCircuit.add(a)
        }

        else -> {
            add(mutableListOf(a, b))
        }
    }
    return true
}

private fun Day08Part1(input: List<String>, connections: Int = 1000): Int {
    val vectors = input.toVecPairs().take(connections)
    val circuits = mutableListOf<MutableList<Vector3D>>()

    for ((a, b) in vectors) {
        if (!circuits.connect(a, b)) continue
    }

    return circuits
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce { a, b -> a * b }
}

private fun Day08Part2(input: List<String>): Long {
    val vectors = input.toVecPairs()
    val circuits = mutableListOf<MutableList<Vector3D>>()
    
    var lastProduct = 0L

    for ((a, b) in vectors) {
        if (!circuits.connect(a, b)) continue

        lastProduct = (a.x * b.x).toLong()
    }

    return lastProduct
}

fun main() {
    val input = input(day = 8)

    Day08Part1(input.sample(), connections = 10) shouldBe 40
    Day08Part1(input.main()) shouldBe 105952

    Day08Part2(input.sample()) shouldBe 25272
    Day08Part2(input.main()) shouldBe 975931446
}