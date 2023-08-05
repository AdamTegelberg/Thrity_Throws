package com.example.thritythrows

import android.widget.ImageButton

class Dice(val diceButton: ImageButton) {

    var value = 1
    var focused = false
    var used = false

    fun roll(){
        value = (1..6).random()

    }


}
