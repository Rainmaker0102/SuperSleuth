/*
Sean Davis & Bradyn Hinman
1 March 2023
Android Project -- SuperSleuth -- Inspired by I SPY books where the user
    is given a color to find through the camera and earns points for
    each successful find

References:
    Android CodeLab for CameraX -> https://developer.android.com/codelabs/camerax-getting-started
    Android Developers Documentation for CameraX (used many sub-pages on this page) ->
        https://developer.android.com/training/camerax
 */
package com.example.supersleuth

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
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