package de.felix0351.aoc2025
import java.io.File

class Bank(val batteries: CharArray) {

    fun getHighestJoltagePartOne(): Int {
        // 1. Gehe durch die Liste von Batterien und merke den Index von dem höchsten Wert.
        // Der letzte Wert kann nicht genommen werden, da noch eine zweite Batterie gebraucht wird.

        var indexFirstBattery = 0
        for (i in 1 until batteries.size-1) {
            if (batteries[i] > batteries[indexFirstBattery]) {
                indexFirstBattery = i
            }
        }

        // 2. Durchgang
        // Suche die zweithöchste Zahl.
        // Hier kann nun erst ab der ersten Batterie angefangen werden. Auch der letzte Wert ist hier drin.
        var indexSecondBattery = indexFirstBattery+1
        for (i in indexFirstBattery+1 until batteries.size) {

            // Wenn es die letzte Batterie in der Liste ist, ist kein Vergleich mehr nötig, da es die einzig mögliche Batterie zum dran schalten ist.
            if (i+1 == batteries.size) {
                continue
            }

            // Vergleiche
            if (batteries[i+1] > batteries[indexSecondBattery]) {
                indexSecondBattery = i+1
            }

        }

        val firstBattery = batteries[indexFirstBattery]
        val secondBattery = batteries[indexSecondBattery]
        return charArrayOf(firstBattery, secondBattery).joinToString("").toInt()
    }

    fun getHighestJoltagePartTwo(): Long {
        // Merke den Index jeder Batterie.
        val batteriesIndex = MutableList(12) { 0 }

        // Durchlauf für jede Batterie welche geschaltet werden muss, also 12.
        for (currentBatteryIndex in 0 until 12) {

            // Nun muss die höchste Ziffer gefunden werden und der Index davon in die Indexliste geschrieben werden.
            // Regel 1: Es muss am Durchlauf zum Ende immer Platz für die kommenden Batterien gelassen werden.
            // z.B. ist beim ersten Durchlauf das Ende batteries.size-11 (11, da noch 11 Batterien kommen).
            // Regel 2: Die nächste Batterie darf erst nach dem vorherigen Index starten.

            // Start Index so setzen, dass es nach der letzten Batterie startet
            // die Erste ausnehmen, da sie ja ab 0 starten kann.
            if (currentBatteryIndex != 0) {
                batteriesIndex[currentBatteryIndex] = batteriesIndex[currentBatteryIndex - 1] + 1
            }

            // Gehe durch die Reihe und vergleiche
            for (i in batteriesIndex[currentBatteryIndex]+1 until batteries.size-(11-currentBatteryIndex)) {

                // Wenn der aktuelle Index größer ist, als das was bisher bekannt ist, updaten
                if (batteries[i] > batteries[batteriesIndex[currentBatteryIndex]]) {
                    batteriesIndex[currentBatteryIndex] = i
                }

            }
        }

        var resultStr = ""
        batteriesIndex.forEach {
            resultStr += batteries[it]
        }

        println("Battery size is $resultStr")

        return resultStr.toLong()
    }



}


fun main() {
    val inputFile = File("puzzles/day3.txt")

    val banks = inputFile.readText()
        .split("\r\n")
        .map { Bank(it.toCharArray()) }

    val joltagePartOne = banks.sumOf { it.getHighestJoltagePartOne() }
    val joltagePartTwo = banks.sumOf { it.getHighestJoltagePartTwo() }

    println("Joltage sum part one: $joltagePartOne")
    println("Joltage sum part two: $joltagePartTwo")

}