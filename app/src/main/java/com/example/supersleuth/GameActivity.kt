package com.example.supersleuth

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.*
import com.google.common.util.concurrent.ListenableFuture
import java.nio.ByteBuffer
import java.util.concurrent.Executors

class GameActivity : AppCompatActivity() {
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }






    private var gameScore: Int = 0
    private var highScoreVal: Int = 0
    private var sleuthSearchName: String = "Red"
    private var sleuthSearchColor: String = "FF0000"

    /*
    White 	    #FFFFFF 	rgb(255, 255, 255)
    Silver 	    #C0C0C0 	rgb(192, 192, 192)
    Gray 	    #808080 	rgb(128, 128, 128)
    Black 	    #000000 	rgb(0, 0, 0)
    Red 	    #FF0000 	rgb(255, 0, 0)
    Maroon 	    #800000 	rgb(128, 0, 0)
    Yellow 	    #FFFF00 	rgb(255, 255, 0)
    Olive 	    #808000 	rgb(128, 128, 0)
    Lime 	    #00FF00 	rgb(0, 255, 0)
    Green 	    #008000 	rgb(0, 128, 0)
    Aqua 	    #00FFFF 	rgb(0, 255, 255)
    Teal 	    #008080 	rgb(0, 128, 128)
    Blue 	    #0000FF 	rgb(0, 0, 255)
    Navy 	    #000080 	rgb(0, 0, 128)
    Fuchsia 	#FF00FF 	rgb(255, 0, 255)
    Purple 	    #800080 	rgb(128, 0, 128)
    */

    private val colorWhite : String= "FFFFFF"
    private val colorSilver : String= "C0C0C0"
    private val colorGray : String= "808080"
    private val colorBlack : String= "000000"
    private val colorRed : String= "FF0000"
    private val colorMaroon : String= "800000"
    private val colorYellow : String= "FFFF00"
    private val colorOlive : String= "808000"
    private val colorLime : String= "00FF00"
    private val colorGreen : String= "008000"
    private val colorAqua : String= "00FFFF"
    private val colorTeal : String= "008080"
    private val colorBlue : String= "0000FF"
    private val colorNavy : String= "000080"
    private val colorFuchsia : String= "FF00FF"
    private val colorPurple : String= "800080"

    private val colorList = listOf(colorWhite, colorSilver, colorGray, colorBlack, colorRed, colorMaroon, colorYellow, colorOlive, colorLime, colorGreen, colorAqua, colorTeal, colorBlue, colorNavy, colorFuchsia, colorPurple)

    private val leniencyVal : Int = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this@GameActivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val highScore =intent.getStringExtra("highscore")
        if (highScore != null) {
            highScoreVal = highScore.toInt()
        }

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            startCamera(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startCamera(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder()
            .build()

        val viewFinder: PreviewView = findViewById(R.id.previewView)

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        var imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(1280, 720))
            .build()


        imageAnalyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { image: ImageProxy ->
            // Get the center pixel of the image
            val centerX = image.width / 2
            val centerY = image.height / 2

            val bitmap = image. createBitmap(1280,720)
            val color:Int? = bitmap.getPixel(centerX, centerY)

//            val buffer = image.planes[0].buffer
//            val pixelStride = image.planes[0].pixelStride
//            val rowStride = image.planes[0].rowStride
//            val rowPadding = rowStride - pixelStride * image.width
//            val pixelBuffer = ByteBuffer.allocate(pixelStride)
//            val rgba = IntArray(4)

//            buffer.position(centerY * rowStride + centerX * pixelStride)
//            for (i in 0 until 4) {
//                pixelBuffer.position(0)
//                buffer.get(pixelBuffer.array())
//                rgba[i] = pixelBuffer.getInt(0)
//            }

            // Convert the RGBA value to a human-readable format
//            val red = (rgba[0] and 0xff)
//            val green = (rgba[1] and 0xff)
//            val blue = (rgba[2] and 0xff)
//            val alpha = (rgba[3] and 0xff)
            val red:Int? = color?.red
            val green:Int? = color?.green
            val blue:Int? = color?.blue
            val alpha:Int? = color?.alpha

            // Do something with the RGBA value
            Log.d("ImageAnalyzer", "RGBA: $red, $green, $blue, $alpha")

            // Close the image
            image.close()
        }

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageAnalyzer)
        preview.setSurfaceProvider(viewFinder.surfaceProvider)
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

    fun colorCheck(valCheckColor: String) {
        val redCheck = (valCheckColor.substring(0, 1)).toLong(radix = 16)
        val greenCheck = (valCheckColor.substring(2, 3)).toLong(radix = 16)
        val blueCheck = (valCheckColor.substring(4, 5)).toLong(radix = 16)

        val redGoal = (sleuthSearchColor.substring(0, 1)).toLong(radix = 16)
        val greenGoal = (sleuthSearchColor.substring(2, 3)).toLong(radix = 16)
        val blueGoal = (sleuthSearchColor.substring(4, 5)).toLong(radix = 16)

        if (((redCheck-leniencyVal)<redGoal)&&((redCheck+leniencyVal)>redGoal)) {
            if (((greenCheck-leniencyVal)<greenGoal)&&((greenCheck+leniencyVal)>greenGoal)) {
                if (((blueCheck-leniencyVal)<blueGoal)&&((blueCheck+leniencyVal)>blueGoal)) {
                    gameScore+=1;
                    newColor(sleuthSearchColor)
                }
            }
        }
    }

    fun newColor(oldColor: String) {
        var randomElement = oldColor
        var colorName = ""
        while (randomElement==oldColor) {
            randomElement = colorList.random()
        }
        when(randomElement) {
            colorWhite->colorName = "White"
            colorSilver->colorName = "Silver"
            colorGray->colorName = "Gray"
            colorBlack->colorName = "Black"
            colorRed->colorName = "Red"
            colorMaroon->colorName = "Maroon"
            colorYellow->colorName = "Yellow"
            colorOlive->colorName = "Olive"
            colorLime->colorName = "Lime"
            colorGreen->colorName = "Green"
            colorAqua->colorName = "Aqua"
            colorTeal->colorName = "Teal"
            colorBlue->colorName = "Blue"
            colorNavy->colorName = "Navy"
            colorFuchsia->colorName = "Fuchsia"
            colorPurple->colorName = "Purple"
        }
    }
}