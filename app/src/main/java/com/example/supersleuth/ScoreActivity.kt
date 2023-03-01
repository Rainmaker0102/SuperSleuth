package com.example.supersleuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {
    private var highScore: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        val scoreText=findViewById<TextView>(R.id.textViewScore)
        val scoreText2=findViewById<TextView>(R.id.textViewHighScore)
        val gameScore =intent.getStringExtra("score")
        val highScore =intent.getStringExtra("highscore")
        scoreText.text= gameScore.toString()
        scoreText2.text= highScore.toString()
    }

    fun replayTime(view: View) {
        // Do something in response to button click
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("highscore", highScore)
        startActivity(intent)
        finish()
    }

    fun mainTime(view: View) {
        // Do something in response to button click
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("highscore", highScore)
        startActivity(intent)
        finish()
    }
}