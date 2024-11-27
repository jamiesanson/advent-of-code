package mmxxiii

import io.kotest.matchers.shouldBe
import mmxxiii.Workflow.Result.*
import mmxxiii.input.input
import util.Input

private typealias Part = Map<Char, Int>

private data class Workflow(
    val name: Name,
    val rules: List<Rule>,
    val ruleset: Ruleset,
) {
    @JvmInline
    value class Name(val name: String) {
        companion object {
            val IN = Name("in")
        }
    }

    operator fun invoke(part: Part): Result = ruleset(part)

    sealed class Result {
        data class Continue(val name: Name) : Result()
        data object Accept : Result()
        data object Reject : Result()
    }
}

private typealias Ruleset = (Part) -> Workflow.Result

private data class ParseResult(
    val workflows: Map<Workflow.Name, Workflow>,
    val parts: List<Part>,
)

private fun String.toWorkflow(): Workflow {
    val name = Workflow.Name(takeWhile { it != '{' })

    val conditions = dropWhile { it != '{' }
        .removeSurrounding("{", "}")
        .split(",")
        .map(String::toCondition)

    return Workflow(
        name = name,
        rules = conditions,
        ruleset = { part -> conditions.firstNotNullOf { condition -> condition.apply(part) } }
    )
}

private fun String.toResult(): Workflow.Result = when (this) {
    "R" -> Reject
    "A" -> Accept
    else -> Continue(Workflow.Name(this))
}

private sealed interface Rule {
    val result: Workflow.Result
    val apply: (Part) -> Workflow.Result?

    data class Default(override val result: Workflow.Result) : Rule {
        override val apply: (Part) -> Workflow.Result? = { result }
    }

    data class If(
        val bounds: Pair<Char, IntRange>,
        override val apply: (Part) -> Workflow.Result?,
        override val result: Workflow.Result,
    ) : Rule
}

private fun String.toCondition(): Rule {
    // Terminal conditions
    if (!contains(":")) {
        return Rule.Default(toResult())
    }

    val partChar = take(1).first()
    val equality = drop(1).take(1).first()
    val size = drop(2).takeWhile { it != ':' }.toInt()
    val result = takeLastWhile { it != ':' }.toResult()

    return Rule.If(
        bounds = partChar to when (equality) {
            '>' -> size + 1..4000
            '<' -> 1 until size
            else -> error("Unrecognised equality: $equality")
        },
        apply = { part ->
            val partSize = part[partChar]!!

            when {
                equality == '>' && partSize > size -> result
                equality == '<' && partSize < size -> result
                else -> null
            }
        },
        result = result
    )
}

private fun String.toPart(): Part {
    return trim()
        .removeSurrounding("{", "}")
        .split(",")
        .map { it.split("=") }
        .associate { (char, value) -> char.first() to value.toInt() }
}

private fun parseInput(input: List<String>): ParseResult {
    return ParseResult(
        workflows = input.takeWhile { it.isNotEmpty() }
            .map(String::toWorkflow)
            .associateBy { it.name },
        parts = input.takeLastWhile { it.isNotEmpty() }.map(String::toPart)
    )
}

fun Day19Part1(input: List<String>): Int {
    val (workflows, parts) = parseInput(input)

    val workflowResults = parts.map { part ->
        var result = workflows[Workflow.Name.IN]!!(part)

        while (result is Continue) {
            result = workflows[result.name]!!(part)
        }

        part to result
    }

    return workflowResults
        .filter { (_, result) -> result == Accept }
        .sumOf { (part, _) -> part.values.sum() }
}

private typealias Bounds = Map<Char, IntRange>

private fun Bounds.splitBy(range: Pair<Char, IntRange>): Pair<Bounds, Bounds> {
    val (dimension, bound) = range
    val lower = toMutableMap().apply {
        compute(dimension) { _, current ->
            current!!
            if (bound.first == 1) {
                current.first..bound.last
            } else {
                current.first..bound.first
            }
        }
    }

    val upper = toMutableMap().apply {
        compute(dimension) { _, current ->
            current!!
            if (bound.first == 1) {
                bound.last..current.last
            } else {
                (bound.last + 1)..current.last
            }
        }
    }

    return if (bound.first == 1) lower to upper else upper to lower
}

private fun Pair<Char, IntRange>.intersects(bounds: Bounds): Boolean {
    val (dimension, range) = this
    val dimensionBounds = bounds[dimension]!!

    val size = if (range.first == 1) range.last else range.first - 1

    return size in dimensionBounds
}

fun Day19Part2(input: List<String>): Long {
    val (workflows, _) = parseInput(input)

    val acceptedBounds = mutableListOf<Bounds>()

    val toProcess = mutableListOf<Pair<Bounds, Workflow.Result>>(
        "xmas".associateWith { 1..4000 } to Continue(Workflow.Name.IN)
    )

    while (toProcess.isNotEmpty()) {
        val (bounds, instruction) = toProcess.removeFirst()

        when (instruction) {
            Accept -> {
                acceptedBounds += bounds
                println("$instruction: $bounds")
                continue
            }

            Reject -> {
                println("$instruction: $bounds")
                continue
            }

            is Continue -> {
                var remaining = bounds
                val (_, rules, _) = workflows[instruction.name]!!

                for (rule in rules.filterIsInstance<Rule.If>()) {
                    if (rule.bounds.intersects(remaining)) {
                        val (inside, outside) = remaining.splitBy(rule.bounds)
                        toProcess.add(inside to rule.result)
                        remaining = outside
                    }
                }

                val defaultResult = rules.filterIsInstance<Rule.Default>().first().result

                toProcess.add(remaining to defaultResult)
            }
        }
    }

    return acceptedBounds.sumOf {
        it.values.fold(1L) { acc, range -> acc * range.count() }
    }
}

fun main() {
    val input = input(day = 19)

    Day19Part1(input.sample()) shouldBe 19114
    Day19Part1(input.main()) shouldBe 480738
                                      //31777236337464L
    Day19Part2(input.sample()) shouldBe 167409079868000L
}