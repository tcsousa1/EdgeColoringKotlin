# Edge Coloring Algorithm in Kotlin

This repository contains an implementation of an **edge coloring algorithm** for graphs, written in Kotlin. The solution is based on the edge adjacency matrix approach, inspired by the work of C. Paul Shyni et al.

## 🚀 Features

- Efficient graph representation using edge adjacency matrix
- Heuristic edge coloring algorithm
- Can be adapted to read graphs in DIMACS format or custom formats
- Tested on classic graph coloring benchmarks

## 🛠️ Technologies

- [Kotlin](https://kotlinlang.org/)
- Command-line based execution (no external dependencies)

## 📦 How to Use

### 1. Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/edge-coloring-kotlin.git
cd edge-coloring-kotlin
```

### 2. Compile the Kotlin file:

```bash
kotlinc edge_coloring.kt -include-runtime -d edge_coloring.jar
```

### 3. Run the program:

```bash
java -jar edge_coloring.jar
```

> Note: Replace with your actual filenames and parameters.

## 🧪 Example

```bash
java -jar edge_coloring.jar input_graph.txt
```

## 📁 Project Structure

```
edge-coloring-kotlin/
├── edge_coloring.kt
├── README.md
└── .gitignore (optional)
```

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

Developed by [Thiago Cannabrava de Sousa](https://github.com/tcsousa1).
