package com.example.thritythrows

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.toPersistableBundle

typealias ScoreMap = Map<String, Int>

/**
 * keeps track of the current score of the game as well as the overall game per round.
 * Implements Parcelable to be able to save the state of the class through config changes
 */
class ScoreTracker() : Parcelable {

    var currentScore = 0
    var pointMap = mutableMapOf<String, Int>()


    fun updateScore(value: Int) {
        currentScore += value
    }

    constructor(score: Int, points: Map<String, Int>) : this() {
        this.currentScore = score
        this.pointMap = points as MutableMap<String, Int>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(currentScore)
        parcel.writeMap(pointMap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScoreTracker> {
        override fun createFromParcel(parcel: Parcel): ScoreTracker {
            return ScoreTracker(
                parcel.readInt(),
                parcel.readHashMap(Map::class.java.classLoader) as ScoreMap
            )
        }

        override fun newArray(size: Int): Array<ScoreTracker?> {
            return arrayOfNulls(size)
        }
    }

}