package com.example.supersleuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class GameActivity : AppCompatActivity() {
    private var gameScore: Int = 0
    private var highScoreVal: Int = 0
    private var sleuthSearchColor: String = "FF0000"

    /*
    White 	#FFFFFF 	rgb(255, 255, 255)
    Silver 	#C0C0C0 	rgb(192, 192, 192)
    Gray 	#808080 	rgb(128, 128, 128)
    Black 	#000000 	rgb(0, 0, 0)
    Red 	#FF0000 	rgb(255, 0, 0)
    Maroon 	#800000 	rgb(128, 0, 0)
    Yellow 	#FFFF00 	rgb(255, 255, 0)
    Olive 	#808000 	rgb(128, 128, 0)
    Lime 	#00FF00 	rgb(0, 255, 0)
    Green 	#008000 	rgb(0, 128, 0)
    Aqua 	#00FFFF 	rgb(0, 255, 255)
    Teal 	#008080 	rgb(0, 128, 128)
    Blue 	#0000FF 	rgb(0, 0, 255)
    Navy 	#000080 	rgb(0, 0, 128)
    Fuchsia 	#FF00FF 	rgb(255, 0, 255)
    Purple 	#800080 	rgb(128, 0, 128)
    */

    private val colorValWhite: String = "FF0000"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val highScore =intent.getStringExtra("highscore")
        if (highScore != null) {
            highScoreVal = highScore.toInt()
        }
    }

    fun concedeTime(view: View) {
        // Do something in response to button click
        if(gameScore > highScoreVal) {
            highScoreVal = gameScore
        }
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("score", gameScore.toString())
        intent.putExtra("highscore", highScoreVal.toString())
        startActivity(intent)
        finish()
    }

    fun newColor(oldColor: String) {

    }
}