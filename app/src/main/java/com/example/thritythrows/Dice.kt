package com.example.thritythrows

import android.widget.ImageButton

/**
 * A class representing a dice with a value (1 to 6) and is either focused or not focused.
 */
class Dice(val diceButton: ImageButton) {

    var value = 1
    var focused = false

    fun roll() {
        value = (1..6).random()

    }

    /**
     * Sets the image of the dice to the correct red image based on the value.
     */
    fun setImageRed() {
        val image: Int = when (value) {
            1 -> R.drawable.red1
            2 -> R.drawable.red2
            3 -> R.drawable.red3
            4 -> R.drawable.red4
            5 -> R.drawable.red5
            else -> R.drawable.red6
        }
        diceButton.setImageResource(image)
    }

    /**
     * Sets the image of the dice to the correct image based on the value.
     * Grey of not focused and white if focused
     */
    fun setImage() {
        val image: Int
        if (focused) {
            image = when (value) {
                1 -> R.drawable.grey1
                2 -> R.drawable.grey2
                3 -> R.drawable.grey3
                4 -> R.drawable.grey4
                5 -> R.drawable.grey5
                else -> R.drawable.grey6
            }
        } else {
            image = when (value) {
                1 -> R.drawable.white1
                2 -> R.drawable.white2
                3 -> R.drawable.white3
                4 -> R.drawable.white4
                5 -> R.drawable.white5
                else -> R.drawable.white6
            }
        }
        diceButton.setImageResource(image)
    }


}
