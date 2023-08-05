package com.example.thritythrows

class ScoreCalculator {


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






