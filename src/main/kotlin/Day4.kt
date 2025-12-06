package org.example

import java.io.File

val accessPoints = arrayListOf(
    intArrayOf(-1, -1), // Nach oben, links
    intArrayOf(-1, 0), // Nach oben
    intArrayOf(-1, 1), // Nach oben, rechts

    intArrayOf(0, 1), // Nach rechts
    intArrayOf(0, -1), // Nach links

    intArrayOf(1, -1), // Nach unten, links
    intArrayOf(1, 0), // Nach unten
    intArrayOf(1, 1) // Nach unten rechts
)

fun goTroughGrid(grid: List<CharArray>): List<IntArray> {
    // Erstelle ein identisches Grid, welches die Anzahl an benachbarten Papierrollen für jeden Wert enthält
    val resultValues = MutableList(grid.size) { IntArray(grid[0].size) { -1 } }

    // Gehe das Grid durch
    for (iRow in 0 until grid.size) {
        for (iColumn in 0 until grid[0].size) {
            // Alle Character mit '.' werden natürlich übersprungen
            if (grid[iRow][iColumn] == '.') continue

            var adjacentPaperRolls = 0

            val checkIndexes = accessPoints.map {
                intArrayOf(
                    it[0] + iRow,
                    it[1] + iColumn
                )
            }

            // Prüfe für alle 8 Positionen, ob eine Papierrolle darauf liegt.
            // Wenn der Index außerhalb des Grids liegt, ist der Platz frei
            for (checkIndex in checkIndexes) {
                if (
                    (checkIndex[0] >= 0) &&
                    (checkIndex[0] < grid.size) &&
                    (checkIndex[1] >= 0) &&
                    (checkIndex[1] < grid[0].size)
                    ) {
                    // Position ist im Grid, somit kann auf Papierrolle geprüft werden.
                    if (grid[checkIndex[0]][checkIndex[1]] == '@') {
                        adjacentPaperRolls++
                    }
                }
            }

            // Für jeden Wert gibt es einen Eintrag in der ResultList, wie viele Papierrollen nebenan sind
            resultValues[iRow][iColumn] = adjacentPaperRolls
        }
    }

    return resultValues
}

fun checkPartOne(grid: List<CharArray>, debug: Boolean = false): Pair<Int, List<List<Boolean>>> {
    // Erstelle ein Grid, welches die Anzahl an benachbarten Papierrollen für jeden Wert enthält.
    val resultGrid = goTroughGrid(grid)

    // Debug Print
    if (debug) {
        resultGrid.forEach { row ->
            row.forEach { value ->
                if (value == -1) print(".")
                else print(value.digitToChar())
            }
            print("\n")
        }
    }

    // Transformiere zu einer Liste von Booleans. True, wenn sie erreichbar sind, also weniger als 4 benachbarte Papierrollen haben und kein Punkt sind (-1).
    val accessibleRolls = resultGrid.map { row ->
        row.map { value ->
            value != -1 && value < 4
        }
    }

    // Zähle die Papierrollen, welche verfügbar sind.
    val countRemovableRolls =  accessibleRolls.sumOf { row ->
        row.count { it }
    }

    // Gebe die Anzahl an entfernbaren Rollen an sowie das Boolean-Grid für Part Zwei
    return Pair(countRemovableRolls, accessibleRolls)
}

fun checkPartTwo(grid: List<CharArray>, debug: Boolean = false): Int {
    var countRemovedRolls = 0
    var currentGrid = grid

    // Wiederhole den Prozess aus Part 1 solange, bis keine Rolle mehr frei wird
    do {
        // Zähle alle entfernten Rollen
        val (removableCount, accessibleRolls) = checkPartOne(currentGrid)
        countRemovedRolls += removableCount

        // Debug output
        if (debug) {
            currentGrid.forEach { println(it.joinToString("")) }
            println("\n\nRemove $removableCount rolls of paper.")
        }

        // Nun müssen alle Werte aus dem ursprünglichen Grid auf '.' gesetzt werden, welche auf True stehen
        currentGrid = currentGrid.mapIndexed { iRow, row ->
            row.mapIndexed { iColumn, value ->
                if (accessibleRolls[iRow][iColumn]) '.'
                else value
            }.toCharArray()
        }

    } while (removableCount > 0) // Solange Papierrollen entfernt werden können.

    return countRemovedRolls
}





fun main() {
    val inputFile = File("puzzles/day4.txt")
        .readText()
        .split("\r\n")
        .map { it.toCharArray() }

    val (resultPartOne, _) = checkPartOne(inputFile)
    val resultPartTwo = checkPartTwo(inputFile, debug = true)

    println("Ergebnis Teil 1: $resultPartOne")
    println("Ergebnis Teil 2: $resultPartTwo")
}