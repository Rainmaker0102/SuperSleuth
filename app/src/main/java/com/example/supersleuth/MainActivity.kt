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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import kotlin.system.exitProcess
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture


class MainActivity : AppCompatActivity() {
    private var highScoreVal: String = "0"

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val highScore =intent.getStringExtra("highscore")
        if (highScore != null) {
            highScoreVal = highScore
        }

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

//        preview.setSurfaceProvider(previewView.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
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