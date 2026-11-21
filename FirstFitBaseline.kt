import java.io.File
import java.util.BitSet

// Sequential first-fit edge coloring baseline.
// Each edge (in input order) receives the smallest color not yet used at either endpoint.
// Memory: O(|E| + |V|·Δ) bits; time: O(|E|·Δ).
// Usage: run with no arguments to process all instances below, or pass instance file names as arguments.

val defaultInstances = listOf(
    "dsjc250.5.col", "dsjc500.1.col", "dsjc500.5.col", "dsjc500.9.col", "dsjc1000.1.col", "dsjc1000.5.col",
    "r250.5.col", "r1000.5.col", "dsjr500.1c.col", "dsjr500.5.col", "le450_25c.col", "le450_25d.col",
    "flat300_28_0.col", "flat1000_50_0.col", "flat1000_60_0.col", "flat1000_76_0.col", "latin_square.col",
    "dsjc1000.9.col", "r1000.1c.col", "C2000.5.col", "C4000.5.col"
)

fun runInstance(fileName: String): String {
    val resource = ClassLoader.getSystemResource(fileName) ?: error("Instance not found: $fileName")
    val file = File(resource.toURI())

    val us = ArrayList<Int>()
    val vs = ArrayList<Int>()
    var n = 0
    file.forEachLine { line ->
        val p = line.trim().split(Regex("\\s+"))
        when (p[0]) {
            "p" -> n = p[2].toInt()
            "e" -> { us.add(p[1].toInt()); vs.add(p[2].toInt()) }
        }
    }
    val m = us.size
    val degree = IntArray(n + 1)
    for (i in 0 until m) { degree[us[i]]++; degree[vs[i]]++ }
    val maxDegree = degree.maxOrNull() ?: 0

    val start = System.nanoTime()
    val used = Array(n + 1) { BitSet(2 * maxDegree) }
    val coloring = IntArray(m)
    var colors = 0
    for (i in 0 until m) {
        val u = us[i]; val v = vs[i]
        var c = 0
        while (used[u].get(c) || used[v].get(c)) c++
        coloring[i] = c
        used[u].set(c); used[v].set(c)
        if (c + 1 > colors) colors = c + 1
    }
    val executionTimeMs = (System.nanoTime() - start) / 1e6

    // Validity check: no vertex sees the same color twice
    val seen = Array(n + 1) { BitSet() }
    var valid = true
    for (i in 0 until m) {
        val c = coloring[i]
        if (seen[us[i]].get(c) || seen[vs[i]].get(c)) { valid = false; break }
        seen[us[i]].set(c); seen[vs[i]].set(c)
    }

    val ratio = colors.toDouble() / maxDegree
    println("%-20s edges=%8d maxDeg=%5d colors=%5d colors/maxDeg=%.3f time=%.1f ms valid=%b"
        .format(fileName, m, maxDegree, colors, ratio, executionTimeMs, valid))
    return "%s,%d,%d,%d,%.3f,%.1f,%b".format(fileName, m, maxDegree, colors, ratio, executionTimeMs, valid)
}

fun main(args: Array<String>) {
    val instances = if (args.isNotEmpty()) args.toList() else defaultInstances
    val lines = mutableListOf("instance,edges,max_degree,colors,colors_per_max_degree,time_ms,valid")
    for (name in instances) lines.add(runInstance(name))
    File("baseline_first_fit.csv").writeText(lines.joinToString("\n") + "\n")
    println("Results written to baseline_first_fit.csv")
}
