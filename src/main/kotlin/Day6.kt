package de.felix0351.aoc2025

import java.io.File
import kotlin.text.replace



fun<T: Any> List<List<T>>.transpose(): List<List<T>> {

    // Prüfe auch gleiche Länge bei allen Arrays
    val sizeToCheck = this[0].size
    if (all { it.size != sizeToCheck }) {
        throw IllegalArgumentException("All elements must have same size.")
    }

    val output = MutableList(sizeToCheck) { mutableListOf<T>()  }
    // Gehe durch alle Zeilen
    for (i in 0 until size) {
        // Gehe durch alle Spalten
        for (j in 0 until sizeToCheck) {
            output[j].add(i, this[i][j])
        }
    }

    return output
}

fun List<Long>.reduceWithOperator(op: String): Long =
    reduce { acc, next ->
        if (op == "*") acc * next
        else acc + next
    }


fun calculatePartOne(input: List<String>): Long {
    // Formatiere den input, entferne zu viele Leerzeichen und trenne die Zahlen
    val formattedInput = input.map {
            it.trim()
            .replace("\\s+".toRegex(), " ")
            .split(" ")
    }

    // Erstelle eine Liste mit allen Operatoren
    val operators = formattedInput[formattedInput.size-1]


    // Erstelle eine Liste mit Zahlen.
    // Diese werden reihenweise eingelesen und dann Spalten und Reihen getauscht
    val lines = formattedInput
        .slice(0..<formattedInput.size-1)
        .map { it.map(String::toLong) }
        .transpose()

    // Addiere / multipliziere die Reihen durch und gebe die Summe zurück
    val sum = lines
        .zip(operators)
        .sumOf { (numbers, operator) -> 
            numbers.reduceWithOperator(operator)
        }

    return sum
}

fun calculatePartTwo(input: List<String>): Long {

    // Ersteinmal jede Ziffer zu einem Char Array umwandeln und die einzelnen Reihen drehen, da von hinten gezählt wid.
    val formattedInput = input
        .slice(0..<input.size-1)
        .map { it.reversed().toCharArray().asList() }
        

    // Erstelle eine Liste mit allen Operatoren
    val operators = input[input.size-1]
        .trim()
        .replace("\\s+".toRegex(), " ")
        .split(" ")
        .reversed()

    // Dann Reihen und Spalten tauschen, sodass eine Liste mit den Zahlen zum Rechnen entsteht
    val transposed = formattedInput.transpose()

    
    val blockNumbers = mutableListOf<Long>()
    var currentOperatorIndex = 0
    val currentNumbers = mutableListOf<Long>()

    // Gehe durch die Liste
    for (numberStr in transposed) {

        // 1. Prüfen, ob es sich um einen Übergang zum nächsten Block handelt
        if (numberStr.all(Char::isWhitespace)) {

            // Berechnen der Blockzahl
            val blockNumber = currentNumbers.reduceWithOperator(operators[currentOperatorIndex])
            blockNumbers.add(blockNumber)

            // Zum nächsten Block gehen
            currentOperatorIndex++
            currentNumbers.clear()
            continue
        }

        // 2. Char Liste in Zahl umwandeln
        val number = numberStr
            .joinToString(separator = "")
            .trim()
            .toLong()

        // 3. Nummer hinzufügen
        currentNumbers.add(number)
    }
    
    // Letzter Block wurde noch nicht hinzugefügt
    return blockNumbers.sum() + currentNumbers.reduceWithOperator(operators[currentOperatorIndex])
}


fun main() {
    val input = File("puzzles/day6.txt")
        .readLines()

    val partOne = calculatePartOne(input)
    val partTwo = calculatePartTwo(input)
    println("Teil Eins: $partOne")
    println("Teil Zwei: $partTwo")

}