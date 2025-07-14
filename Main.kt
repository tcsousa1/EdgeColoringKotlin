import java.io.File

data class Edge(val u: Int, val v: Int)

fun main() {
    val fileName = "dsjc250.5.col" // <- change to desired instance name
    val file = File(ClassLoader.getSystemResource(fileName)!!.file)

    val edges = readDimacsFile(file)
    println("Instance: $fileName")
    println("Number of edges: ${edges.size}")

    val conflictMatrix = buildEdgeAdjacencyMatrix(edges)
    val coloring = IntArray(edges.size) { -1 }

    val start = System.currentTimeMillis()
    var currentColor = 0

    while (hasUncoloredEdges(coloring)) {
        val degrees = computeDegrees(conflictMatrix)
        val maxDegreeEdges = selectEdgesWithHighestDegree(degrees)
            .filter { coloring[it] == -1 } // Only considers uncolored edges

        if (maxDegreeEdges.isEmpty()) break // Safety against infinite loop

//        val nullSubmatrix = findNullSubmatrix(conflictMatrix, maxDegreeEdges)
        val nullSubmatrix = findBestNullSubmatrixGreedy(conflictMatrix, degrees, maxDegreeEdges)
        applyColor(conflictMatrix, degrees, coloring, nullSubmatrix, currentColor)

        println("Round $currentColor: ${nullSubmatrix.size} edges colored with color $currentColor")
        currentColor++
    }

    val end = System.currentTimeMillis()
    val executionTime = end - start

    println("\n--- FINAL RESULT ---")
    println("Total number of colors used: $currentColor")
    println("Execution time: ${executionTime} ms")

    val colorCount = mutableMapOf<Int, Int>()
    for (color in coloring) {
        colorCount[color] = colorCount.getOrDefault(color, 0) + 1
    }

    println("Color usage summary:")
    colorCount.toSortedMap().forEach { (color, qty) ->
        println("Color $color: $qty edges")
    }

    // Conflict check
    if (checkConflicts(edges, coloring)) {
        println("Conflicts found: invalid coloring.")
    } else {
        println("No conflicts found: valid coloring.")
    }

    // Export result to CSV
    val baseName = fileName.substringBefore(".col")
    val outputFile = File("result_${baseName}.csv")
    outputFile.printWriter().use { out ->
        out.println("Instance,$fileName")
        out.println("Number of Edges,${edges.size}")
        out.println("Total Colors Used,$currentColor")
        out.println("Execution Time (ms),$executionTime")
        out.println()
        out.println("Color,Number of Edges")
        colorCount.toSortedMap().forEach { (color, qty) ->
            out.println("$color,$qty")
        }
    }

    println("\nFile 'result_${baseName}.csv' created successfully.")
}

fun readDimacsFile(file: File): List<Edge> {
    val edges = mutableListOf<Edge>()
    file.forEachLine { line ->
        val parts = line.trim().split(" ")
        if (parts.isNotEmpty() && parts[0] == "e") {
            val u = parts[1].toInt()
            val v = parts[2].toInt()
            edges.add(Edge(u, v))
        }
    }
    return edges
}

fun buildEdgeAdjacencyMatrix(edges: List<Edge>): Array<MutableSet<Int>> {
    val n = edges.size
    val matrix = Array(n) { mutableSetOf<Int>() }

    for (i in 0 until n) {
        for (j in i + 1 until n) {
            if (edges[i].sharesVertexWith(edges[j])) {
                matrix[i].add(j)
                matrix[j].add(i)
            }
        }
    }
    return matrix
}

fun Edge.sharesVertexWith(other: Edge): Boolean {
    return this.u == other.u || this.u == other.v || this.v == other.u || this.v == other.v
}

fun computeDegrees(matrix: Array<MutableSet<Int>>): IntArray {
    val degrees = IntArray(matrix.size)
    for (i in matrix.indices) {
        degrees[i] = matrix[i].size
    }
    return degrees
}

fun selectEdgesWithHighestDegree(degrees: IntArray): List<Int> {
    val maxDegree = degrees.maxOrNull() ?: 0
    return degrees.withIndex()
        .filter { it.value == maxDegree }
        .map { it.index }
}

//fun findNullSubmatrix(matrix: Array<MutableSet<Int>>, candidates: List<Int>): List<Int> {
//    val result = mutableListOf<Int>()
//    for (i in candidates) {
//        val conflicts = result.any { j ->
//            matrix[i].contains(j) || matrix[j].contains(i)
//        }
//        if (!conflicts) {
//            result.add(i)
//        }
//    }
//    return result
//}

fun findBestNullSubmatrixGreedy(
    matrix: Array<MutableSet<Int>>,
    degrees: IntArray,
    candidates: List<Int>
): List<Int> {
    val orderings = listOf(
        candidates.sortedByDescending { degrees[it] }, // maior grau primeiro
        candidates.shuffled(),                         // aleatória
        candidates.sortedBy { it }                     // ordem natural
    )

    var best: List<Int> = emptyList()
    var bestDegree = -1

    for (ordering in orderings) {
        val group = mutableListOf<Int>()
        for (i in ordering) {
            if (group.all { !matrix[i].contains(it) }) {
                group.add(i)
            }
        }
        val groupDegree = group.sumOf { degrees[it] }
        if (group.size > best.size || (group.size == best.size && groupDegree > bestDegree)) {
            best = group
            bestDegree = groupDegree
        }
    }

    return best
}




fun applyColor(
    matrix: Array<MutableSet<Int>>,
    degrees: IntArray,
    coloring: IntArray,
    edges: List<Int>,
    color: Int
) {
    for (i in edges) {
        coloring[i] = color
        degrees[i] = 0
        for (j in matrix.indices) {
            matrix[j].remove(i)
        }
        matrix[i].clear()
    }
}

fun hasUncoloredEdges(coloring: IntArray): Boolean {
    return coloring.any { it == -1 }
}

fun checkConflicts(edges: List<Edge>, coloring: IntArray): Boolean {
    for (i in edges.indices) {
        for (j in i + 1 until edges.size) {
            if (edges[i].sharesVertexWith(edges[j]) && coloring[i] == coloring[j]) {
                println("Conflict found between edges $i and $j with color ${coloring[i]}")
                return true
            }
        }
    }
    return false
}
