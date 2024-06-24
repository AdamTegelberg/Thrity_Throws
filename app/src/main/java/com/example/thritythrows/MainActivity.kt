package com.example.thritythrows

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.thritythrows.databinding.ActivityMainBinding
import kotlin.math.round

/**
 *
 * The main screen of the game. Here all the helping classes are instantiated,
 * the buttons are given onClickListeners and the images are put at the correct position.
 */
class MainActivity : AppCompatActivity() {


    private lateinit var scoreTracker: ScoreTracker
    private lateinit var roundTracker: RoundTracker
    private lateinit var diceList: MutableList<Dice>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, callback)

        /**
         * Spinner is used for choosing the different scoring types.
         */
        val spinnerItems = resources.getStringArray(R.array.score_types).toMutableList()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        binding.scoringTypeSpinner.adapter = spinnerAdapter

        val scoreCalculator = ScoreCalculator()
        roundTracker = RoundTracker()

        val dice1 = Dice(binding.dice1)
        val dice2 = Dice(binding.dice2)
        val dice3 = Dice(binding.dice3)
        val dice4 = Dice(binding.dice4)
        val dice5 = Dice(binding.dice5)
        val dice6 = Dice(binding.dice6)
        diceList = mutableListOf(dice1, dice2, dice3, dice4, dice5, dice6)

        /**
         * Checks if there are saved states. If so, the values and instances are collected and restored.
         */
        if (savedInstanceState != null) {
            scoreTracker = savedInstanceState.getParcelable("scoreTracker")!!
            roundTracker.throwsLeft = savedInstanceState.getInt("throws")
            roundTracker.currentRound = savedInstanceState.getInt("round")

            val tempValueList: List<Int> = listOf(
                savedInstanceState.getInt(ScoringActivity.VALUE1),
                savedInstanceState.getInt(ScoringActivity.VALUE2),
                savedInstanceState.getInt(ScoringActivity.VALUE3),
                savedInstanceState.getInt(ScoringActivity.VALUE4),
                savedInstanceState.getInt(ScoringActivity.VALUE5),
                savedInstanceState.getInt(ScoringActivity.VALUE6)
            )

            for (i in 0..5) {
                diceList[i].value = tempValueList[i]
                diceList[i].setImage()
            }

            for (i in spinnerItems) {
                if (scoreTracker.pointMap.keys.contains(i.lowercase())) {
                    spinnerItems.remove(i)
                    spinnerAdapter.notifyDataSetChanged()
                }
            }

        } else {
            scoreTracker = ScoreTracker()
            rollDIce(diceList)
        }


        binding.scoreText.text = getString(R.string.score, scoreTracker.currentScore)
        binding.currentRound.text = getString(R.string.current_round, roundTracker.currentRound)
        binding.throwsLeft.text = getString(R.string.throws_left, roundTracker.throwsLeft)


        addDiceOnClick(diceList)

        /**
         * When the activity is started from the ScoringActivity, the result is collected and updated.
         * This also updates the state of the activity to match the current result of the game
         */
        val contract = ActivityResultContracts.StartActivityForResult()
        val launcher = registerForActivityResult(contract) { result ->
            if (result.resultCode == RESULT_OK) {
                val roundScore = result.data!!.getIntExtra(ScoringActivity.TEMPVALUE, 0)
                scoreTracker.updateScore(roundScore)
                binding.scoreText.text = getString(R.string.score, scoreTracker.currentScore)
                scoreTracker.pointMap[binding.scoringTypeSpinner.selectedItem.toString()
                    .lowercase()] = roundScore
                roundTracker.throwsLeft = 2
                binding.throwsLeft.text = getString(R.string.throws_left, roundTracker.throwsLeft)
                updateSpinner(spinnerItems, binding, spinnerAdapter)
                for (dice in diceList) {
                    dice.focused = false
                }
                rollDIce(diceList)
                if (roundTracker.currentRound == 11) {
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.SCORE, scoreTracker)
                    startActivity(intent)
                }
            }
        }


        /**
         * rolls the dice if there are throws left this turn.
         */
        binding.throwButton.setOnClickListener {
            if (roundTracker.throwsLeft == 0) {
                Toast.makeText(this, "No more throws this turn", Toast.LENGTH_SHORT).show()
            } else {
                rollDIce(diceList)
                roundTracker.throwsLeft -= 1
                binding.throwsLeft.text = getString(R.string.throws_left, roundTracker.throwsLeft)
            }
        }
        /**
         * If the selected scoring type is not "low",
         * navigates to the ScoringActivity and passes the values of the dice to it.
         * Else, updates to the new state.
         */
        binding.endTurnButton.setOnClickListener {

            if (binding.scoringTypeSpinner.selectedItem.toString().lowercase() != "low") {
                val intent = Intent(this, ScoringActivity::class.java)
                intent.putExtra(ScoringActivity.VALUE1, dice1.value)
                intent.putExtra(ScoringActivity.VALUE2, dice2.value)
                intent.putExtra(ScoringActivity.VALUE3, dice3.value)
                intent.putExtra(ScoringActivity.VALUE4, dice4.value)
                intent.putExtra(ScoringActivity.VALUE5, dice5.value)
                intent.putExtra(ScoringActivity.VALUE6, dice6.value)

                intent.putExtra(ScoringActivity.TEMPVALUE, 0)

                intent.putExtra(
                    ScoringActivity.SCORETYPE,
                    binding.scoringTypeSpinner.selectedItem.toString()
                )
                launcher.launch(intent)
            } else {
                scoreTracker.updateScore(scoreCalculator.calculateLow(diceList))
                binding.scoreText.text = getString(R.string.score, scoreTracker.currentScore)
                scoreTracker.pointMap[binding.scoringTypeSpinner.selectedItem.toString()
                    .lowercase()] = scoreCalculator.calculateLow(diceList)
                roundTracker.throwsLeft = 2
                binding.throwsLeft.text = getString(R.string.throws_left, roundTracker.throwsLeft)
                updateSpinner(spinnerItems, binding, spinnerAdapter)
                for (dice in diceList) {
                    dice.focused = false
                }
                rollDIce(diceList)
            }
            roundTracker.updateRound()
            binding.currentRound.text =
                getString(R.string.current_round, roundTracker.currentRound)
        }
    }

    private fun updateSpinner(
        spinnerItems: MutableList<String>,
        binding: ActivityMainBinding,
        spinnerAdapter: ArrayAdapter<String>
    ) {
        spinnerItems.remove(binding.scoringTypeSpinner.selectedItem.toString())
        spinnerAdapter.notifyDataSetChanged()
    }


    private fun rollDIce(list: List<Dice>) {
        for (dice in list) {
            if (!dice.focused) {
                dice.roll()
                dice.setImage()
            }
        }
    }

    /**
     * adds an onClick callback to all dice in the list list.
     * The callback rolls the dice if not focused
     */
    private fun addDiceOnClick(list: List<Dice>) {
        for (dice in list) {
            dice.diceButton.setOnClickListener {
                dice.focused = !dice.focused
                dice.setImage()
            }
        }
    }

    /**
     * saves the current state of the activity.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("scoreTracker", scoreTracker)
        outState.putInt("round", roundTracker.currentRound)
        outState.putInt("throws", roundTracker.throwsLeft)

        outState.putInt(ScoringActivity.VALUE1, diceList[0].value)
        outState.putInt(ScoringActivity.VALUE2, diceList[1].value)
        outState.putInt(ScoringActivity.VALUE3, diceList[2].value)
        outState.putInt(ScoringActivity.VALUE4, diceList[3].value)
        outState.putInt(ScoringActivity.VALUE5, diceList[4].value)
        outState.putInt(ScoringActivity.VALUE6, diceList[5].value)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Exit App")
            builder.setMessage("Are you sure you want to exit the app?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder.show()
        }
    }


}



