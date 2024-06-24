package com.example.thritythrows

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.thritythrows.databinding.ActivityScoringBinding

/**
 * this activity handles the screen where the user gets a score for a round.
 * The user gets to highlight the chosen dice to add up to the chose scoreType.
 */
class ScoringActivity : AppCompatActivity() {


    companion object {
        const val SCORETYPE = "scoringType"
        const val TEMPVALUE = "tempValue"
        const val VALUE1 = "value1"
        const val VALUE2 = "value2"
        const val VALUE3 = "value3"
        const val VALUE4 = "value4"
        const val VALUE5 = "value5"
        const val VALUE6 = "value6"
    }

    var tempScore: Int = 0

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityScoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, callback)

        val usedDiceList = mutableListOf<Dice>()

        /**
         * collects the values passed from the MainActivity in the intent extras.
         */
        val intent = intent
        val extras = intent.extras
        val scoringType = extras?.getString(SCORETYPE)!!
        binding.scoreType.text = getString(R.string.score_type, scoringType)

        val scoreCalculator = ScoreCalculator()
        tempScore = extras.getInt(TEMPVALUE)
        binding.roundScore.text = getString(R.string.round_score, tempScore)

        val values = listOf(
            extras.getInt(VALUE1),
            extras.getInt(VALUE2), extras.getInt(VALUE3),
            extras.getInt(VALUE4), extras.getInt(VALUE5),
            extras.getInt(VALUE6)
        )

        val buttons = listOf(binding.dice1, binding.dice2, binding.dice3, binding.dice4, binding.dice5, binding.dice6)

        val diceList = mutableListOf<Dice>()
        for (i in buttons) {
            diceList.add(Dice(i))
        }
        matchDiceToImage(diceList, values)
        addDiceOnClick(diceList)


        binding.doneButton.setOnClickListener {
            intent.putExtra(TEMPVALUE, tempScore)
            setResult(RESULT_OK, intent)
            finish()
        }

        /**
         * checks that the user follows the rules of the game,
         * i.e. the dice are not already used and the score of the focused dice add upp to the correct value
         */
        binding.calcButton.setOnClickListener {

            if (checkLists(diceList, usedDiceList)) {
                if (scoreCalculator.calculateScore(diceList, scoringType)) {
                    updateScore(scoringType.toInt())
                    binding.roundScore.text = getString(R.string.round_score, tempScore)
                    for (dice in diceList) {
                        if (dice.focused) {
                            dice.focused = false
                            dice.setImageRed()
                            usedDiceList.add(dice)
                            dice.diceButton.setOnClickListener {
                                Toast.makeText(this, "Die is already used", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Choice does not add to $scoringType", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }


    private fun updateScore(value: Int) {
        tempScore += value
    }

    /**
     * put the correct image on the correct dice/button.
     */
    private fun matchDiceToImage(diceList: List<Dice>, values: List<Int?>) {
        for (i in 0..5) {
            if (values[i] != null) {
                diceList[i].value = values[i]!!
                values[i]?.let { diceList[i].setImage() }
            }
        }
    }

    private fun addDiceOnClick(list: List<Dice>) {
        for (dice in list) {
            dice.diceButton.setOnClickListener{
                dice.focused = !dice.focused
                dice.setImage()
            }
        }
    }

    /**
     *  checks if the dice are already used.
     */
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