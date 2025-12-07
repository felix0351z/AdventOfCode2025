package de.felix0351.aoc2025

import java.io.File

// Zahlen beginnend mit 0 vorne werden durch die Umwandlung zwischen Int und String automatisch entfernt.
class IdRange(val firstId: Long, val lastId: Long) {

    var sumPartOne = 0.toLong()
        private set

    var sumPartTwo = 0.toLong()
        private set

    fun checkForValidIds() {

        // Gehe alle Ids durch
        for (id in firstId..lastId) {
            // Wandle in Char Array zum einfacheren Vergleich um
            val chars = id.toString().toCharArray()

            // Entferne Arrays mit nur einem Wert
            if (chars.size == 1) continue

            // -------- Teil Eins --------
            if (isIdInvalidPartOne(chars)) {
                sumPartOne += id
            }

            // -------- Teil Zwei --------
            // Nehme die erste Ziffer als Identifier
            if (isIdInvalidPartTwo(chars, charArrayOf(chars[0]))) {
                // Wenn die Id nicht korrekt ist, addiere sie zum Ergebnis
                sumPartTwo += id
            }
        }

    }

    /**
     * Überprüfe auf doppelte Werte.
     * Der Char-Array wird in der Mitte geteilt und beide Teile werden verglichen.
     * @return true, wenn beide Teile identisch sind, ansonsten false
     **/
    private fun isIdInvalidPartOne(chars: CharArray): Boolean{
        val firstHalf = chars.slice(0 until chars.size/2)
        val secondHalf = chars.slice(chars.size / 2 until chars.size)

        return firstHalf.joinToString() == secondHalf.joinToString()
    }

    /**
     * Erweiterung von Part Eins also eigener Algorithmus.
     * @param chars: Die zu prüfende Id als Char-Array
     * @param identifier Id-Teil, auf welchen überprüft werden soll. z.B. 12 für 121212
     *
     * Der Algorithmus funktioniert folgendermaßen:
     * 1. Starte mit der ersten Ziffer der Id als Identifier.
     * 2. Gehe die Reihe Ziffer für Ziffer durch.
     * 3. Wenn die nächste Ziffer gleich ist, geht er immer weiter.
     * 4. Wenn er jedoch auf eine Ziffer stößt, welche nicht gleich ist z.B. 113, wird die Funktion
     * nochmals aufgerufen (rekursiv) und der Identifier um die nächste Ziffer erweitert.
     *
     * Dies wird so lange gemacht bis entweder er an das Ende kommt und merkt es geht nicht auf
     * - Das Ende von dem zu überprüfenden Wert ist über der Größe des Arrays
     * - oder bei einem weiteren Durchgang wäre der Identifier über der Größe des Arrays
     *
     * oder es passt alles und er verlässt erfolgreich die Schleife, dann entspricht die ID dem Muster.
     *
     **/
    private fun isIdInvalidPartTwo(chars: CharArray, identifier: CharArray): Boolean {
        // Prüfe ob der Identifier mehrmals vorkommt.
        for (idx in 0 until chars.size-identifier.size step identifier.size) {

            // Erstelle Start- und Endindex von dem folgenden zu überprüfenden Wert.
            val startNext = idx + identifier.size
            val end = idx + (identifier.size * 2 - 1)

            // Wenn der End-Index schon über dem Array ist, entspricht die Id nicht dem Muster
            // und es kann aufgehört werden.
            if (end >= chars.size) return false

            // Prüfe den nächsten Wert im Array
            val next = chars.slice(startNext..end)
            if (identifier.joinToString() != next.joinToString()) {
                // Hier: Die Werte waren nicht gleich

                // Wenn der Identifier bereits die Länge vom Array erreicht hat, ist hier Schluss
                // und es wurde kein Muster gefunden.
                if (identifier.size+1 >= chars.size) {
                    return false
                }

                // Ansonsten probiere ein neuer Durchlauf mit neuem Identifier.
                val newIdentifier = identifier.plus(chars[identifier.size])
                return isIdInvalidPartTwo(chars, newIdentifier)
            }

        }

        // Wenn ein Durchgang ohne Unterbrechungen stattgefunden hat, stimmt die Id.
        return true
    }



}

fun main() {
    val input = File("puzzles/day2.txt")

    // Lese den Text und wandle alle Reichweiten in Range Objekte um
    val ranges = input.readText()
        .split(",")
        .map {
            val split = it.split("-")
            IdRange(split[0].toLong(), split[1].toLong())
        }

    // Generiere die Summe für jede einzelne Reihe und addiere sie alle auf
    var sumPartOne = 0.toLong()
    var sumPartTwo = 0.toLong()

    for (range in ranges) {
        range.checkForValidIds()
        sumPartOne += range.sumPartOne
        sumPartTwo += range.sumPartTwo
    }

    println("Ergebnis Teil Eins: $sumPartOne")
    println("Ergebnis Teil Eins: $sumPartTwo")
}