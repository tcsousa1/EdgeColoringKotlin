# Edge Coloring Algorithm in Kotlin

This repository contains an implementation of an **edge coloring algorithm** for graphs, written in Kotlin. The solution is based on the edge adjacency matrix approach, inspired by the work of C. Paul Shyni et al.

## 🚀 Features

- Graph representation using the edge adjacency matrix (stored as one adjacency set per edge)
- Heuristic edge coloring algorithm
- Reads graphs in DIMACS format
- Tested on classic DIMACS graph coloring benchmarks
- Sequential first-fit baseline for comparison

## 🛠️ Technologies

- [Kotlin](https://kotlinlang.org/)
- Command-line based execution (no external dependencies)

## 📦 How to Use

### 1. Clone the repository

```bash
git clone https://github.com/tcsousa1/edgecoloringkotlin.git
cd edgecoloringkotlin
```

### 2. Get the DIMACS instances

Download the `.col` files from the DIMACS benchmark page (http://cedric.cnam.fr/~porumbed/graphs/) and place them in a `resources/` folder (they are loaded from the classpath).

### 3. Run the edge-adjacency-matrix heuristic (`Main.kt`)

Set the instance name in `main()` (`val fileName = "dsjc250.5.col"`), then:

```bash
kotlinc Main.kt -include-runtime -d edge_coloring.jar
java -Xmx26G -cp edge_coloring.jar:resources MainKt
```

The program prints the number of colors, the execution time and the validity check, and writes `resultado_<instance>.csv`. The execution time covers only the coloring phase (it excludes reading the file, building the edge adjacency structure and the validity check).

### 4. Run the first-fit baseline (`FirstFitBaseline.kt`)

```bash
kotlinc FirstFitBaseline.kt -include-runtime -d baseline.jar
java -cp baseline.jar:resources FirstFitBaselineKt              # all 21 instances
java -cp baseline.jar:resources FirstFitBaselineKt dsjc250.5.col # a single instance
```

Each edge, in input order, receives the smallest color not used at either endpoint. Results are written to `baseline_first_fit.csv`.

## 🧪 Experimental environment (paper)

- MacBook Pro 2019, Intel Core i9 2.3 GHz (8 cores), 32 GB RAM, macOS Sonoma 14.6.1
- Kotlin 2.1.21 (JVM target 1.8), IntelliJ IDEA 2025.1
- OpenJDK 24.0.1, default garbage collector (G1), `-Xmx26G`
- Each instance executed once

## 📁 Project Structure

```
edgecoloringkotlin/
├── Main.kt               # greedy edge coloring based on the edge adjacency matrix
├── FirstFitBaseline.kt   # sequential first-fit baseline
├── README.md
└── LICENSE
```

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

Developed by [Thiago Cannabrava de Sousa](https://github.com/tcsousa1).
