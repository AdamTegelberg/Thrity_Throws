package com.example.thritythrows

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.thritythrows.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerItems = resources.getStringArray(R.array.score_types)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        binding.scoringChoices.adapter = spinnerAdapter

        val scoreTracker = ScoreTracker()

        binding.scoreText.text = getString(R.string.score, scoreTracker.currentScore)
        binding.currentRound.text = getString(R.string.current_round, 1)
        binding.throwsLeft.text = getString(R.string.throws_left, 3)

        val dice1 = Dice(binding.dice1)
        val dice2 = Dice(binding.dice2)
        val dice3 = Dice(binding.dice3)
        val dice4 = Dice(binding.dice4)
        val dice5 = Dice(binding.dice5)
        val dice6 = Dice(binding.dice6)

        val diceList: List<Dice> = mutableListOf(dice1, dice2, dice3, dice4, dice5, dice6)
        addDiceOnClick(diceList)
        rollDIce(diceList)








        binding.throwButton.setOnClickListener {
            rollDIce(diceList)
            binding.scoreText.text = getString(R.string.score, 15)
        }

        binding.endTurnButton.setOnClickListener {
            val intent = Intent(this, ScoringActivity::class.java)
            intent.putExtra("value1", dice1.value)
            intent.putExtra("value2", dice2.value)
            intent.putExtra("value3", dice3.value)
            intent.putExtra("value4", dice4.value)
            intent.putExtra("value5", dice5.value)
            intent.putExtra("value6", dice6.value)

            intent.putExtra("tempValue", 0)

            intent.putExtra("scoringType", binding.scoringChoices.selectedItem.toString())
            startActivity(intent)
        }
    }






    private fun rollDIce(list: List<Dice>) {
        for (dice in list) {
            if (!dice.focused) {
                dice.roll()
                setImage(dice, dice.focused)
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
}



