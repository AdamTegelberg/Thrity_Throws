package com.example.thritythrows

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.thritythrows.databinding.ActivityResultBinding

/**
 * this activity shows the result of the game.
 */
class ResultActivity : AppCompatActivity() {

    companion object {
        const val SCORE = "score"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        val scoreTracker = intent.getParcelableExtra<ScoreTracker>(SCORE)
        val totalScore = scoreTracker!!.currentScore
        val scoreMap = scoreTracker.pointMap

        binding.round1Score.text = getString(R.string.total_result_round, "Low", scoreMap["low"])
        binding.round2Score.text = getString(R.string.total_result_round, "4", scoreMap["4"])
        binding.round3Score.text = getString(R.string.total_result_round, "5", scoreMap["5"])
        binding.round4Score.text = getString(R.string.total_result_round, "6", scoreMap["6"])
        binding.round5Score.text = getString(R.string.total_result_round, "7", scoreMap["7"])
        binding.round6Score.text = getString(R.string.total_result_round, "8", scoreMap["8"])
        binding.round7Score.text = getString(R.string.total_result_round, "9", scoreMap["9"])
        binding.round8Score.text = getString(R.string.total_result_round, "10", scoreMap["10"])
        binding.round9Score.text = getString(R.string.total_result_round, "11", scoreMap["11"])
        binding.round10Score.text = getString(R.string.total_result_round, "12", scoreMap["12"])


        binding.totalScoreText.text = getString(R.string.total_score, totalScore)

    }
}