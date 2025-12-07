package de.felix0351.aoc2025

import java.io.File


/**
 * Stelle die im Puzzle beschriebene Zifferscheibe als Objekt dar.
 * Die Scheibe kann man dann mit den 2 Methoden nach rechts und links drehen.
 *
 * Dabei wird jeweils ein Counter für Teil Eins und Zwei erhöht, wenn die entsprechende Bedingung erfüllt ist.
 */
class Dial {

    private var number = 50

    var counterPartOne = 0
        private set

    var counterPartTwo = 0

    fun turnRight() {
        number++

        if (number == 100) {
            number = 0
        }

        if (number == 0) {
            counterPartTwo++
        }
    }

    fun turnLeft() {
        number--

        if (number == -1) {
            number = 99
        }

        if (number == 0) {
            counterPartTwo++
        }
    }

    fun updateCounterPartOne() {
        if (number == 0) {
            counterPartOne++
        }
    }
}


fun main() {
    val inputFile = File("puzzles/day1.txt")
    val input = inputFile.readLines()

    val dial = Dial()

    for (direction in input) {
        val isLeft = direction.startsWith("L")
        val number = direction.substring(1).toInt()

        if (isLeft) {
            repeat(number) {
                dial.turnLeft()
            }
        } else {
            repeat(number) {
                dial.turnRight()
            }
        }

        dial.updateCounterPartOne()
    }

    println("Passwort für die Nordpol-Basis: ${dial.counterPartOne}")
    println("und mit der Methode 0x...: ${dial.counterPartTwo}")
}