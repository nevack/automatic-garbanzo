import java.util.ArrayDeque

fun main() {

    fun parseLine(line: String): Module {
        val (fromStr, toStr) = line.split(" -> ")
        val outs = toStr.split(", ")

        if (fromStr == Module.BROADCASTER) return Module.Broadcaster(outs)

        val op = fromStr[0]
        val name = fromStr.drop(1)

        return when (op) {
            '%' -> Module.FlipFlop(name, outs)
            '&' -> Module.ConjunctionRaw(name, outs)
            else -> error("ERR!")
        }
    }

    fun parse(input: List<String>): Map<String, Module> {
        val modules = HashMap(input.map { line -> parseLine(line) }.associateBy { it.name })
        for (raw in modules.values.filterIsInstance<Module.ConjunctionRaw>()) {
            val ins = modules.values.filter { it.outs.contains(raw.name) }.map { it.name }
            modules[raw.name] = Module.Conjunction(raw.name, ins, raw.outs)
        }
        return modules
    }

    fun part1(input: List<String>): Long {
        val modules = parse(input)
        var low = 0L
        var high = 0L

        repeat(1000) {
            val broadcaster = checkNotNull(modules[Module.BROADCASTER])

            val actions = ArrayDeque<Action>().apply {
                offer(Action("button", Pulse.LOW, broadcaster))
            }

            while (actions.any()) {
                val (source, pulse, target) = actions.pop()

                when (pulse) {
                    Pulse.LOW -> low++
                    Pulse.HIGH -> high++
                }

                target.apply(pulse, source)?.mapTo(actions) { (newTarget, newPulse) ->
                    Action(target.name, newPulse, modules[newTarget] ?: Module.Output(newTarget))
                }
            }
        }

        return low * high
    }

    fun part2(input: List<String>): Long {
        val modules = parse(input)
        val rx = modules.values.single { "rx" in it.outs }.name
        val interesting = modules.values.filter { rx in it.outs }.map { it.name }
        val occurrences = interesting.associateWith { mutableListOf<Long>() }
        var presses = 0L

        while (true) {
            val broadcaster = checkNotNull(modules[Module.BROADCASTER])
            val actions = ArrayDeque<Action>().apply {
                offer(Action("button", Pulse.LOW, broadcaster))
            }

            while (actions.any()) {
                val (source, pulse, target) = actions.pop()

                if (pulse == Pulse.HIGH && source in interesting) {
                    checkNotNull(occurrences[source]) += presses

                    if (!occurrences.values.any { it.size < 2 }) {
                        return lcm(occurrences.values.map { (a, b) -> b - a })
                    }
                }

                target.apply(pulse, source)?.mapTo(actions) { (newTarget, newPulse) ->
                    Action(target.name, newPulse, modules[newTarget] ?: Module.Output(newTarget))
                }
            }

            presses++
        }
    }

    val testInput1 = readInput("Day20_test1")
    check(part1(testInput1) == 11687500L)

    val input = readInput("Day20")
    timed("Part1 answer") { part1(input) }
    timed("Part2 answer") { part2(input) }
}

private enum class Pulse { LOW, HIGH }

private sealed class Module(
    val name: String,
    val outs: List<String>,
) {
    abstract fun apply(pulse: Pulse, input: String): List<Pair<String, Pulse>>?

    class Broadcaster(outs: List<String>) : Module("broadcaster", outs) {
        override fun apply(pulse: Pulse, input: String): List<Pair<String, Pulse>> {
            return outs.map { out -> out to pulse }
        }
    }

    class FlipFlop(name: String, outs: List<String>) : Module(name, outs) {
        private var on = false
        override fun apply(pulse: Pulse, input: String): List<Pair<String, Pulse>>? = when (pulse) {
            Pulse.HIGH -> null
            Pulse.LOW -> {
                outs.map { out -> out to if (on) Pulse.LOW else Pulse.HIGH}.also {
                    on = !on
                }
            }
        }
    }

    class ConjunctionRaw(name: String, outs: List<String>) : Module(name, outs) {
        override fun apply(pulse: Pulse, input: String): List<Pair<String, Pulse>> = error("ERR!")
    }

    class Conjunction(name: String, ins: List<String>, outs: List<String>) : Module(name, outs) {
        private var memory: MutableMap<String, Pulse> = HashMap(ins.associateWith { Pulse.LOW })

        override fun apply(pulse: Pulse, input: String): List<Pair<String, Pulse>> {
            memory[input] = pulse
            return if (memory.values.all { it == Pulse.HIGH }) {
                outs.map { out -> out to Pulse.LOW }
            } else {
                outs.map { out -> out to Pulse.HIGH }
            }
        }
    }

    class Output(name: String) : Module(name, emptyList()) {
        override fun apply(pulse: Pulse, input: String): List<Pair<String, Pulse>>? = null
    }

    companion object {
        const val BROADCASTER = "broadcaster"
    }
}

private data class Action(
    val name: String,
    val pulse: Pulse,
    val module: Module,
)
