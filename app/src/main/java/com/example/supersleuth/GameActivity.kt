package com.example.supersleuth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import com.google.mlkit.vision.common.internal.ImageConvertUtils

class GameActivity : AppCompatActivity() {
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                // Left empty because compiler complains otherwise
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                //Left empty because compiler complains otherwise
            }
        }






    private var gameScore: Int = 0
    private var highScoreVal: Int = 0
    private var sleuthSearchName: String = "Red"
    private var sleuthSearchColor: String = "#FF0000"       //Must append # to beginning of variable

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

    private val colorWhite : String= "#FFFFFF"
    private val colorSilver : String= "#C0C0C0"
    private val colorGray : String= "#808080"
    private val colorBlack : String= "#000000"
    private val colorRed : String= "#FF0000"
    private val colorMaroon : String= "#800000"
    private val colorYellow : String= "#FFFF00"
    private val colorOlive : String= "#808000"
    private val colorLime : String= "#00FF00"
    private val colorGreen : String= "#008000"
    private val colorAqua : String= "#00FFFF"
    private val colorTeal : String= "#008080"
    private val colorBlue : String= "#0000FF"
    private val colorNavy : String= "#000080"
    private val colorFuchsia : String= "#FF00FF"
    private val colorPurple : String= "#800080"

    private val colorList = listOf(colorWhite, colorSilver, colorGray, colorBlack, colorRed, colorMaroon, colorYellow, colorOlive, colorLime, colorGreen, colorAqua, colorTeal, colorBlue, colorNavy, colorFuchsia, colorPurple)

    private val leniencyVal : Int = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this@GameActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val sleuthLabelName=findViewById<TextView>(R.id.textView3)
        sleuthLabelName.text=sleuthSearchName
        sleuthLabelName.setBackgroundColor(Color.parseColor(sleuthSearchColor))

        val highScore =intent.getStringExtra("highscore")
        if (highScore != null) {
            highScoreVal = highScore.toInt()
        }

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            startCamera(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startCamera(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder()
            .build()

        val viewFinder: PreviewView = findViewById(R.id.previewView)

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalyzer = ImageAnalysis.Builder()
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(480, 640))
            .build()


        imageAnalyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { image: ImageProxy ->
            val rotationDegrees = image.imageInfo.rotationDegrees

            val mediaImage = image.getImage();
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            val bitmap = ImageConvertUtils.getInstance().getUpRightBitmap(image)

            // Get the center pixel of the image
            val centerX = image.width / 2
            val centerY = image.height / 2
            val width = image.width
            val height = image.height
            val planes = image.planes
            val format = image.format
            val buffer = image.planes[0].buffer
//            val buffer = image.planes[0].buffer.array()
            val pixelStride = image.planes[0].pixelStride
            val rowStride = image.planes[0].rowStride
//            buffer.position(centerY * rowStride + centerX * pixelStride)

            val redBuffer = image.planes[0].buffer[0].toInt()
            val greenBuffer = image.planes[0].buffer[1].toInt()
            val blueBuffer = image.planes[0].buffer[2].toInt()
            val hex = String.format("#%02x%02x%02x", redBuffer, greenBuffer, blueBuffer)

//            val pixelData = buffer[(height * rowStride + width * pixelStride) / 2]


//            val rowPadding = rowStride - pixelStride * image.width
//            val pixelBuffer = ByteBuffer.allocate(pixelStride)
//            val rgba = IntArray(4)

//            for (i in 0 until 4) {
//                pixelBuffer.position(0)
//                buffer.get(pixelBuffer.array())
//                rgba[i] = pixelBuffer.getInt(0)
//            }

            // Convert the RGBA value to a human-readable format
            //
//            val red = (rgba[0] and 0xff)
//            val green = (rgba[1] and 0xff)
//            val blue = (rgba[2] and 0xff)
//            val alpha = (rgba[3] and 0xff)
//            val red = (rgba[0]).toString(16)
//            val green = (rgba[1]).toString(16)
//            val blue = (rgba[2]).toString(16)
//            val alpha = (rgba[3]).toString(16)
//            val red = rgba[0] and 0xff
//            val green = rgba[1] and 0xff
//            val blue = rgba[2] and 0xff
//            val hex = String.format("#%02x%02x%02x", red, green, blue)

//            Log.d("GameActivity:ImageAnalysis", "RGBA: $red, $green, $blue, $alpha")
//            Log.d("Game:Activity:ImageAnalysis", "Hex RGB: $hex")

            val alphaPlane = image.planes[0].buffer[0]
            val redPlane = image.planes[0].buffer[1]
            val greenPlane = image.planes[0].buffer[2]
            val bluePlane = image.planes[0].buffer[3]

            // Do something with the RGBA value
            Log.d("GameActivity:ImageAnalysis", "RGBA Planes: $redPlane, $greenPlane, $bluePlane, $alphaPlane")

            val imageWhole = image.planes[0].buffer

            Log.d("GameActivity:ImageWhole", "Image Whole: $imageWhole")

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
        val redCheck = (valCheckColor.substring(1, 2)).toLong(radix = 16)
        val greenCheck = (valCheckColor.substring(3, 4)).toLong(radix = 16)
        val blueCheck = (valCheckColor.substring(5, 6)).toLong(radix = 16)

        val redGoal = (sleuthSearchColor.substring(1, 2)).toLong(radix = 16)
        val greenGoal = (sleuthSearchColor.substring(3, 4)).toLong(radix = 16)
        val blueGoal = (sleuthSearchColor.substring(5, 6)).toLong(radix = 16)

        if (((redCheck-leniencyVal)<redGoal)&&((redCheck+leniencyVal)>redGoal)) {
            if (((greenCheck-leniencyVal)<greenGoal)&&((greenCheck+leniencyVal)>greenGoal)) {
                if (((blueCheck-leniencyVal)<blueGoal)&&((blueCheck+leniencyVal)>blueGoal)) {
                    gameScore+=1
                    newColor(sleuthSearchColor)
                }
            }
        }
    }

    fun newColor(oldColor: String) {
        var randomElement = oldColor
        while (randomElement==oldColor) {
            randomElement = colorList.random()
        }
        when(randomElement) {
            colorWhite->{
                sleuthSearchColor = "#FFFFFF"
                sleuthSearchName = "White" }
            colorSilver->{
                sleuthSearchColor = "#C0C0C0"
                sleuthSearchName = "Silver" }
            colorGray->{
                sleuthSearchColor = "#808080"
                sleuthSearchName = "Gray" }
            colorBlack->{
                sleuthSearchColor = "#000000"
                sleuthSearchName = "Black" }
            colorRed->{
                sleuthSearchColor = "#FF0000"
                sleuthSearchName = "Red" }
            colorMaroon->{
                sleuthSearchColor = "#800000"
                sleuthSearchName = "Maroon" }
            colorYellow->{
                sleuthSearchColor = "#FFFF00"
                sleuthSearchName = "Yellow" }
            colorOlive->{
                sleuthSearchColor = "#808000"
                sleuthSearchName = "Olive" }
            colorLime->{
                sleuthSearchColor = "#00FF00"
                sleuthSearchName = "Lime" }
            colorGreen->{
                sleuthSearchColor = "#008000"
                sleuthSearchName = "Green" }
            colorAqua->{
                sleuthSearchColor = "#00FFFF"
                sleuthSearchName = "Aqua" }
            colorTeal->{
                sleuthSearchColor = "#008080"
                sleuthSearchName = "Teal" }
            colorBlue->{
                sleuthSearchColor = "#0000FF"
                sleuthSearchName = "Blue" }
            colorNavy->{
                sleuthSearchColor = "#000080"
                sleuthSearchName = "Navy" }
            colorFuchsia->{
                sleuthSearchColor = "#FF00FF"
                sleuthSearchName = "Fuchsia" }
            colorPurple->{
                sleuthSearchColor = "#800080"
                sleuthSearchName = "Purple" }
        }
        val sleuthLabelName=findViewById<TextView>(R.id.textView3)
        sleuthLabelName.text=sleuthSearchName
        sleuthLabelName.setBackgroundColor(Color.parseColor(sleuthSearchColor))
    }
}
