package com.example.supersleuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var highScore: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        highScore= intent.getStringExtra("highscore")!!
    }

    fun playTime(view: View) {
        // Do something in response to button click
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("highscore", highScore)
        startActivity(intent)
        finish()
    }

    fun quitTime(view: View) {
        // Do something in response to button click
        exitProcess(0)
    }
}