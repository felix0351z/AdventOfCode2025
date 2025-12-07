package de.felix0351.aoc2025

import java.io.File
import kotlin.math.max
import kotlin.math.min

object PartOne {

    fun countFreshIds(ids: List<Long>, ranges: List<LongRange>, debug: Boolean = false): Int {
        var freshIds = 0

        // Prüfe für jede Id, ob sie in einen der Range-Listen vorkommt
        idLoop@ for (id in ids) {

            for (range in ranges) {
                if (id in range) {
                    freshIds++
                    // Wenn die id in einer der Listen vorkommt, kann zur nächsten Id gegangen werden.
                    continue@idLoop
                }
            }
        }

        return freshIds
    }

}

object PartTwo {

    fun ClosedRange<Long>.containedIn(other: ClosedRange<Long>) =
        (min(endInclusive, other.endInclusive) - max(start, other.start))

    fun ClosedRange<Long>.merge(other: ClosedRange<Long>) =
        LongRange(
            min(start, other.start),
            max(endInclusive, other.endInclusive)
        )

    fun mergeIdRanges(ranges: MutableList<ClosedRange<Long>>): List<ClosedRange<Long>> {
        for (i in 1 until ranges.size) {
            for (j in ranges.indices) {
                // Nicht mit sich selbst vergleichen
                if (i == j) continue

                // Prüfe, ob die aktuelle Range sich mit der zu Vergleichenden überschneidet.
                val diff = ranges[i].containedIn(ranges[j])
                if (diff < 0.0) {
                    // Überschneiden sich nicht => keine Range zusammenführen
                    continue
                }

                // Hier: Ranges über schneien sich => zusammenführen
                ranges[i] = ranges[i].merge(ranges[j])
                ranges.removeAt(j)

                // Wenn germerged, wiederhole den Prozess, bis es keine Ranges mehr zu mergen gibt.
                return mergeIdRanges(ranges)
            }
        }

        return ranges
    }

}

fun main() {
    val (rangesStr, idsStr) = File("puzzles/day5.txt")
        .readText()
        .split("\r\n\r\n")

    val ids = idsStr
        .split("\r\n")
        .map(String::toLong)

    val ranges = rangesStr
        .split("\r\n")
        .map {
            val (startId, endId) = it.split("-")
            LongRange(startId.toLong(), endId.toLong())
        }


    val freshIds = PartOne.countFreshIds(ids, ranges)

    // Zähle alle Ranges zusammen (+1, da das Ende auch noch mit dazuzählt)
    val allPossibleFreshIds = PartTwo
        .mergeIdRanges(ranges.toMutableList())
        .sumOf { it.endInclusive + 1 - it.start }

    println("Anzahl frische Ids: $freshIds")
    println("Anzahl aller möglichen Ids: $allPossibleFreshIds")

}