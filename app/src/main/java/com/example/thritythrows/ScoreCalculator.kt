package com.example.thritythrows

/**
 * A class that calculates the score of select dice.
 */
class ScoreCalculator {

    /**
     * calculates the score of the dice in diceList based on the score type scoreType.
     */
    fun calculateScore(diceList: List<Dice>, scoreType: String): Boolean {
        val sT = scoreType.toInt()
        var score = 0

        for (dice in diceList) {
            if (dice.focused) {
                score += dice.value
            }
        }
        return score == sT
    }

    /**
     * calculates the score for when scoreType is low
     */
    fun calculateLow(diceList: List<Dice>): Int {
        var list = mutableListOf<Int>()
        var sum = 0

        for (dice in diceList) {
            list.add(dice.value)
        }
        list = list.sorted().toMutableList()

        for (i in 0..2) {
            sum += list[i]
        }
        return sum
    }
}






