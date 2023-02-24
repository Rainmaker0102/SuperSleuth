package com.example.supersleuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var highScoreVal: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val highScore =intent.getStringExtra("highscore")
        if (highScore != null) {
            highScoreVal = highScore
        }
    }

    fun playTime(view: View) {
        // Do something in response to button click
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("highscore", highScoreVal)
        startActivity(intent)
        finish()
    }

    fun quitTime(view: View) {
        // Do something in response to button click
        exitProcess(0)
    }
}