package com.example.thritythrows

/**
 * a class to keep track of current round and amount of throws left this round.
 */
class RoundTracker {

    var currentRound = 1

    var throwsLeft = 3
    fun updateRound() {
        currentRound += 1
    }


}