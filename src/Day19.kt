fun main() {
    fun parseOutcome(outcomeStr: String): Outcome {
        return when (outcomeStr) {
            "A" -> Outcome.Accept
            "R" -> Outcome.Reject
            else -> Outcome.Workflow(outcomeStr)
        }
    }

    fun parseRule(ruleStr: String): Rule {
        if (":" !in ruleStr) {
            return Rule.Unconditional(parseOutcome(ruleStr))
        }

        val (conditionStr, outcomeStr) = ruleStr.split(":")

        val characteristicChar = conditionStr[0]
        val signChar = conditionStr[1]
        val numberStr = conditionStr.drop(2)

        return Rule.Conditional(characteristicChar, signChar, numberStr.toInt(), parseOutcome(outcomeStr))
    }

    fun parseWorkflow(workflowStr: String) : Workflow {
        val match = checkNotNull(WORKFLOW_REGEX.matchEntire(workflowStr))
        val (name, rulesStr) = match.destructured
        val rules = rulesStr.split(",").map { parseRule(it) }
        return Workflow(name, rules)
    }

    fun parsePart(partStr: String) : Part {
        val match = checkNotNull(PART_REGEX.matchEntire(partStr))
        val (x, m, a, s) = match.destructured
        return Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
    }

    fun parse(input: List<String>): Pair<List<Workflow>, List<Part>> {
        val workflows = mutableListOf<Workflow>()
        val parts = mutableListOf<Part>()

        for (line in input) {
            when {
                line.isEmpty() -> continue
                line.matches(PART_REGEX) -> parts += parsePart(line)
                line.matches(WORKFLOW_REGEX) -> workflows += parseWorkflow(line)
                else -> error("ERR!")
            }
        }

        return workflows to parts
    }

    fun part1(input: List<String>): Int {
        val (workflows, parts) = parse(input)

        val mapping = workflows.associateBy { it.name }
        val accepted = mutableListOf<Part>()

        for (part in parts) {
            var workflow = checkNotNull(mapping["in"])

            while (true) {
                val rule = workflow.rules.first { rule ->
                    rule.matches(part)
                }

                when (val outcome = rule.outcome) {
                    is Outcome.Workflow -> {
                        workflow = checkNotNull(mapping[outcome.name])
                    }
                    is Outcome.Accept -> {
                        accepted += part
                        break
                    }
                    is Outcome.Reject -> {
                        break
                    }
                }
            }
        }

        println(accepted)

        return accepted.sumOf { part -> part.x + part.m + part.a + part.s }
    }

    val testInput1 = readInput("Day19_test1")
    check(part1(testInput1) == 19114)

    val input = readInput("Day19")
    timed("Part1 answer") { part1(input) }
    // timed("Part2 answer") { part2(input) }
}

private val WORKFLOW_REGEX = """(.+)\{(.+)}""".toRegex()
private val PART_REGEX = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex()

private data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int,
)

private data class Workflow(
    val name: String,
    val rules: List<Rule>,
)

private sealed class Rule(val outcome: Outcome) {
    abstract fun matches(part: Part): Boolean

    class Unconditional(outcome: Outcome) : Rule(outcome) {
        override fun matches(part: Part): Boolean = true
    }

    class Conditional(val char: Char, val sign: Char, val number: Int, outcome: Outcome) : Rule(outcome) {
        override fun matches(part: Part): Boolean {
            val partChar = when(char) {
                'x' -> part.x
                'm' -> part.m
                'a' -> part.a
                's' -> part.s
                else -> error("ERR!")
            }

            return when (sign) {
                '>' -> partChar > number
                '<' -> partChar < number
                else -> error("ERR!")
            }
        }
    }
}

private sealed class Outcome {
    class Workflow(val name: String): Outcome()
    data object Accept : Outcome()
    data object Reject : Outcome()
}
