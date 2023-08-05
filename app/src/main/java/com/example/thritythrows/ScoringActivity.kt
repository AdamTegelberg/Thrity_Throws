package com.example.thritythrows

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thritythrows.databinding.ActivityScoringBinding

class ScoringActivity : AppCompatActivity() {


    var tempScore: Int = 0

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityScoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usedDiceList = mutableListOf<Dice>()

        val intent = intent
        val extras = intent.extras
        val scoringType = extras?.getString("scoringType")!!
        binding.scoreType.text = getString(R.string.score_type, scoringType)

        val scoreCalculator = ScoreCalculator()
        tempScore = extras.getInt("tempValue")

        val values = listOf(
            extras.getInt("value1"),
            extras.getInt("value2"), extras.getInt("value3"),
            extras.getInt("value4"), extras.getInt("value5"),
            extras.getInt("value6")
        )

        val buttons = listOf(binding.dice1, binding.dice2, binding.dice3, binding.dice4, binding.dice5, binding.dice6)

        val diceList = mutableListOf<Dice>()
        for (i in buttons) {
            diceList.add(Dice(i))
        }
        matchDiceToImage(diceList, values)
        addDiceOnClick(diceList)


        binding.doneButton.setOnClickListener {
            finish()
        }

/* Change the color of used dice to red. Make a Toast class for prettier toast messages.
 Also try to make this function look nicer. And make a class for the set image functions, they are used in main activity as well */
        binding.calcButton.setOnClickListener {
            if (scoringType != "Low") {
                if (checkLists(diceList, usedDiceList)) {
                    if (scoreCalculator.calculateScore(diceList, scoringType)) {
                        updateScore(scoringType.toInt())
                        binding.roundScore.text = getString(R.string.round_score, tempScore)
                        for (dice in diceList) {
                            if (dice.focused) {
                                dice.focused = false
                                setImage(dice, false)
                                usedDiceList.add(dice)
                                dice.diceButton.setOnClickListener {
                                    Toast.makeText(this, "Die is already used", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "Choice does not add to $scoringType", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "checkList error", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun updateScore(value: Int) {
        tempScore += value
    }

    private fun setImageWhite(dice: Dice, value: Int) {
        val image: Int = when (value) {
            1 -> R.drawable.white1
            2 -> R.drawable.white2
            3 -> R.drawable.white3
            4 -> R.drawable.white4
            5 -> R.drawable.white5
            else -> R.drawable.white6
        }
        dice.diceButton.setImageResource(image)
    }

    private fun setImage(dice: Dice, isFocused: Boolean) {
        val image: Int
        if (isFocused) {
            image = when (dice.value) {
                1 -> R.drawable.grey1
                2 -> R.drawable.grey2
                3 -> R.drawable.grey3
                4 -> R.drawable.grey4
                5 -> R.drawable.grey5
                else -> R.drawable.grey6
            }
        } else {
            image = when (dice.value) {
                1 -> R.drawable.white1
                2 -> R.drawable.white2
                3 -> R.drawable.white3
                4 -> R.drawable.white4
                5 -> R.drawable.white5
                else -> R.drawable.white6
            }
        }
        dice.diceButton.setImageResource(image)
    }

    private fun matchDiceToImage(diceList: List<Dice>, values: List<Int?>) {
        for (i in 0..5) {

            if (values[i] != null) {
                diceList[i].value = values[i]!!
                values[i]?.let { setImageWhite(diceList[i], it) }
            }
        }
    }

    private fun addDiceOnClick(list: List<Dice>) {
        for (dice in list) {
            dice.diceButton.setOnClickListener{
                dice.focused = !dice.focused
                setImage(dice, dice.focused)
            }
        }
    }

    private fun checkLists(diceList: List<Dice>, usedDiceList: List<Dice>): Boolean {
        if (usedDiceList.isNotEmpty()) {
            for (dice1 in diceList) {
                if (dice1.focused) {
                    for (dice2 in usedDiceList) {
                        if (dice1 == dice2) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }
}