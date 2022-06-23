package com.pg.justbalance.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.pg.justbalance.R
import com.pg.justbalance.databinding.CameraLayoutBinding
import com.pg.justbalance.mlkit.GraphicOverlay
import com.pg.justbalance.mlkit.TextGraphic
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias CornersListener = () -> Unit

class CameraFragment : Fragment(R.layout.camera_layout) {

    private lateinit var mTextButton: Button
    private lateinit var mSelectedImage: Bitmap
    private lateinit var mGraphicOverlay: GraphicOverlay

    private var mImageView: ImageView? = null


    private var mImageMaxWidth: Int? = null
    private var mImageMaxHeight:Int? = null


    private lateinit var safeContext: Context

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService


    private lateinit var binding: CameraLayoutBinding


    private lateinit var imgCaptureExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                // cut and paste the previous startCamera() call here.
                startCamera()
            } else {
                Snackbar.make(
                    binding.root,
                    "The camera permission is required",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }

    /**
     * Number of results to show in the UI.
     */
    private val RESULTS_TO_SHOW = 3

    /**
     * Dimensions of inputs.
     */
    private val DIM_BATCH_SIZE = 1
    private val DIM_PIXEL_SIZE = 3
    private val DIM_IMG_SIZE_X = 224
    private val DIM_IMG_SIZE_Y = 224

    private val sortedLabels = PriorityQueue<Map.Entry<String, Float>>(
        RESULTS_TO_SHOW,
        object : Comparator<Map.Entry<String?, Float?>?> {
            override fun compare(
                o1: Map.Entry<String?, Float?>?,
                o2: Map.Entry<String?, Float?>?
            ): Int {
                return o1?.value!!.compareTo(o2?.value!!)
            }
        })

    /* Preallocated buffers for storing image data. */
    private val intValues = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)




    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.camera_layout, container, false)


        cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        mGraphicOverlay = binding.graphicOverlay
        cameraProviderResult.launch(android.Manifest.permission.CAMERA)

        binding.viewFinder.implementationMode = PreviewView.ImplementationMode.COMPATIBLE;

        mImageView = binding.imageView2
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTakePicture.setOnClickListener {
            takePhoto()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

               // animateFlash()
            }}
        binding.btnReadText.setOnClickListener {
            runTextRecognition()
        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun startCamera() {
        // listening for data from the camera
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // connecting a preview use case to the preview in the xml file.
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()

            try {
                // clear all the previous use cases first.
                cameraProvider.unbindAll()
                // binding the lifecycle of the camera to the lifecycle of the application.
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun takePhoto(){
        imageCapture?.let{
            //Create a storage location whose fileName is timestamped in milliseconds.
            val fileName = "JPEG_${System.currentTimeMillis()}"

            val file = File(activity!!.externalMediaDirs[0],fileName)

            // Save the image in the above file
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            /* pass in the details of where and how the image is taken.(arguments 1 and 2 of takePicture)
            pass in the details of what to do after an image is taken.(argument 3 of takePicture) */

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
                        Log.i(TAG,"The image has been saved in ${file.toUri()}")


                        activity!!.runOnUiThread {
                            val matrix = Matrix()
                            matrix.setRotate(90f)

                            val bitmap: Bitmap = BitmapFactory.decodeFile(file.path)

                            val bitmapToUse = Bitmap.createBitmap(bitmap,0, 0, bitmap.width, bitmap.height, matrix, true)
                            binding.imageView2.setImageBitmap(bitmapToUse)
                            mSelectedImage = bitmapToUse
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            binding.root.context,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "Error taking photo:$exception")
                    }

                })

        }
//        var bImage = BitmapFactory.decodeResource(getResources(), imageCapture!!.imageFormat)
//        binding.imageView2.setImageBitmap(bImage)


    }
//    private fun takePhoto() {
//        // Get a stable reference of the modifiable image capture use case
//        val imageCapture = imageCapture ?: return
//
//        // Create timestamped output file to hold the image
//        val photoFile = File(
//            outputDirectory,
//            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
//        )
//
//        // Create output options object which contains file + metadata
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        // Setup image capture listener which is triggered after photo has
//        // been taken
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(safeContext),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val savedUri = Uri.fromFile(photoFile)
//                    val msg = "Photo capture succeeded: $savedUri"
//                    Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
//                }
//            })
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }


    override fun onPause() {
        super.onPause()
        isOffline = true
    }

    override fun onResume() {
        super.onResume()
        isOffline = false
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (allPermissionsGranted()) {
//                //startCamera()
//            } else {
//                Toast.makeText(
//                    safeContext,
//                    "Permissions not granted by the user.",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }


    companion object {
        val TAG = "CameraFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        var isOffline = false
    }

    private class CornerAnalyzer(private val listener: CornersListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            if (!isOffline) {
                listener()
            }
            imageProxy.close() // important! if it is not closed it will only run once
        }

    }
    //MLKIT Functions
    private fun runTextRecognition() {
        mTextButton = binding.btnReadText
        val image = InputImage.fromBitmap(mSelectedImage, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        mTextButton.setEnabled(false)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                mTextButton.setEnabled(true)
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e -> // Task failed with an exception
                mTextButton.setEnabled(true)
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks = texts.textBlocks
        if (blocks.size == 0) {
            showToast("No text found")
            return
        }
        mGraphicOverlay.clear()
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    val textGraphic: GraphicOverlay.Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    mGraphicOverlay.add(textGraphic)
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(safeContext, message, Toast.LENGTH_SHORT).show()
    }

    // Functions for loading images from app assets.

    // Functions for loading images from app assets.
    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private fun getImageMaxWidth(): Int {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = mImageView?.getWidth()
        }
        return mImageMaxWidth as Int
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private fun getImageMaxHeight(): Int? {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight = mImageView?.getHeight()
        }
        return mImageMaxHeight
    }

    // Gets the targeted width / height.
    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode = getImageMaxWidth()
        val maxHeightForPortraitMode = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode!!
        return Pair(targetWidth, targetHeight)
    }

    fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        mGraphicOverlay.clear()
        when (position) {
            0 -> mSelectedImage = getBitmapFromAsset(safeContext, "Please_walk_on_the_grass.jpg")!!
            1 ->                 // Whatever you want to happen when the thrid item gets selected
                mSelectedImage = getBitmapFromAsset(safeContext, "grace_hopper.jpg")!!
        }
        if (mSelectedImage != null) {
            // Get the dimensions of the View
            val targetedSize = getTargetedWidthHeight()
            val targetWidth = targetedSize.first
            val maxHeight = targetedSize.second

            // Determine how much to scale down the image
            val scaleFactor: Float = Math.max(
                mSelectedImage.getWidth().toFloat() / targetWidth.toFloat(),
                mSelectedImage.getHeight().toFloat() / maxHeight.toFloat()
            )
            val resizedBitmap = Bitmap.createScaledBitmap(
                mSelectedImage,
                (mSelectedImage.getWidth() / scaleFactor).toInt(),
                (mSelectedImage.getHeight() / scaleFactor).toInt(),
                true
            )
            mImageView?.setImageBitmap(resizedBitmap)
            mSelectedImage = resizedBitmap
        }
    }

    fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing
    }

    fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val assetManager = context.assets
        val `is`: InputStream
        var bitmap: Bitmap? = null
        try {
            `is` = assetManager.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }




}